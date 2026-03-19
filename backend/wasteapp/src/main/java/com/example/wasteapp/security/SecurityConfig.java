package com.example.wasteapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 *
 * http://localhost:8080/api/auth/register
 * PROGRAMSKI ENTITET: configuration class
 *
 * SVRHA:
 * - Konfigurira Spring Security za REST API (React SPA + backend API).
 * - Nema HTML login forme, nema redirecta.
 * - Neautentificirani zahtjevi dobivaju 401/403 (standard za API).
 */
@Configuration
public class SecurityConfig {

    /**
     * PROGRAMSKI ENTITET: Bean metoda (SecurityFilterChain)
     * <p>
     * SVRHA:
     * - Definira "pravila prolaza" za HTTP zahtjeve.
     * - Prvo prolazi kroz Security filtere, tek onda ide u Controller.
     */
    @Bean
    /**public SecurityFilterChain filterChain(HttpSecurity http) throws Exception*/
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {


            http

                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                    // REST API tipično ne koristi CSRF jer ne radi s browser formama + session cookies
                    // (kasnije, kad bude JWT, ovo je i dalje OK).
                    .csrf(csrf -> csrf.disable())

                    // Ne želimo HTML login stranicu ni redirecte (web-mode)
                    .formLogin(form -> form.disable())

                    // Ne želimo HTTP Basic popup?? (provjeriti što s tim)
                    //.httpBasic(Customizer.withDefaults())

                    //REST “bez popup-a”
                    .httpBasic(basic -> basic.disable())

                    // REST API pravilo: stateless (server ne pamti session)
                    // (kasnije, kad dodaš JWT, ovo je obavezno)
                    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)


                    // Pravila autorizacije (tko smije na koji URL)
                    .authorizeHttpRequests(auth -> auth
                            // Otvoreni auth endpointi
                            .requestMatchers("/api/auth/**").permitAll()

                            // Preporuka: pusti /error da se ne zakomplicira kod exceptiona
                            .requestMatchers("/error").permitAll()

                            // role-based pravila
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")

                            // Sve ostalo traži autentifikaciju
                            .anyRequest().authenticated()
                    );

            return http.build();
    }

    /**
     * PROGRAMSKI ENTITET: Bean metoda (CorsConfigurationSource)
     *
     * SVRHA:
     * - Dopušta React frontendu s localhost:5173 pristup backend API-ju.
     * - to treba za razvoj SPA aplikacije.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Frontend adresa (Vite React app)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Dozvoljene HTTP metode
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Dozvoljeni headeri
        configuration.setAllowedHeaders(List.of("*"));

        // Važno za Authorization header (Bearer token)
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));

        // Ako se koristi cookies/session, ovo bi trebalo na true.
        // Kod JWT Bearer pristupa nije nužno, pa je stavljeno na false.
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}

