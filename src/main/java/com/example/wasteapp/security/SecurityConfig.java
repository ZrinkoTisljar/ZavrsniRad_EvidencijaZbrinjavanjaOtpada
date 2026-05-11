package com.example.wasteapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
 *
 * PROGRAMSKI ENTITET: configuration class
 *
 * SVRHA:
 * - Konfigurira Spring Security za REST API aplikaciju.
 * - Backend se koristi kao REST API za React SPA frontend.
 * - Aplikacija ne koristi HTML login formu ni serverske sesije.
 * - Autentifikacija se temelji na JWT tokenu.
 * - Neautentificirani ili neautorizirani zahtjevi vraćaju HTTP statuse 401/403.
 */
@Configuration
public class SecurityConfig {

    /**
     * PROGRAMSKI ENTITET: Bean metoda
     * TIP: SecurityFilterChain
     *
     * SVRHA:
     * - Definira sigurnosni lanac kroz koji prolaze HTTP zahtjevi.
     * - Konfigurira CORS, CSRF, login način rada, session politiku i JWT filter.
     * - Određuje koji endpointi su javni, a koji zahtijevaju autentifikaciju ili ADMIN rolu.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {

        http
                /**
                 * PROGRAMSKI ENTITET: metoda konfiguracije
                 *
                 * SVRHA:
                 * - Uključuje CORS pravila definirana u metodi corsConfigurationSource().
                 * - Omogućuje da React frontend može slati zahtjeve prema backend API-ju.
                 */
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                /**
                 * PROGRAMSKI ENTITET: sigurnosna konfiguracija
                 *
                 * SVRHA:
                 * - CSRF zaštita se isključuje jer aplikacija koristi stateless REST API.
                 * - Kod JWT autentifikacije ne koristi se klasična browser forma ni server-side session.
                 */
                .csrf(csrf -> csrf.disable())

                /**
                 * PROGRAMSKI ENTITET: sigurnosna konfiguracija
                 *
                 * SVRHA:
                 * - Isključuje se Spring Security HTML login forma.
                 * - Backend ne prikazuje login stranicu, nego prima JSON zahtjeve s React frontenda.
                 */
                .formLogin(form -> form.disable())

                /**
                 * PROGRAMSKI ENTITET: sigurnosna konfiguracija
                 *
                 * SVRHA:
                 * - Isključuje se HTTP Basic autentifikacija.
                 * - Time se izbjegava prikaz browser popup prozora za unos korisničkih podataka.
                 */
                .httpBasic(basic -> basic.disable())

                /**
                 * PROGRAMSKI ENTITET: session konfiguracija
                 *
                 * SVRHA:
                 * - Postavlja aplikaciju u stateless način rada.
                 * - Server ne pamti korisničku sesiju.
                 * - Svaki zaštićeni zahtjev mora poslati JWT token u Authorization headeru.
                 */
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /**
                 * PROGRAMSKI ENTITET: sigurnosni filter
                 *
                 * SVRHA:
                 * - JWT filter se izvršava prije standardnog UsernamePasswordAuthenticationFilter-a.
                 * - Filter čita Bearer token iz Authorization headera.
                 * - Ako je token ispravan, korisnik se postavlja u Spring Security Context.
                 */
                .addFilterBefore(
                        jwtAuthFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                )

                /**
                 * PROGRAMSKI ENTITET: autorizacijska pravila
                 *
                 * SVRHA:
                 * - Definira pristup pojedinim endpointima.
                 * - Auth endpointi su javni.
                 * - Admin endpointi zahtijevaju ADMIN rolu.
                 * - Svi ostali endpointi zahtijevaju prijavljenog korisnika.
                 */
                .authorizeHttpRequests(auth -> auth

                        /**
                         * PROGRAMSKI ENTITET: HTTP metoda OPTIONS
                         *
                         * SVRHA:
                         * - Dopušta CORS preflight zahtjeve iz browsera.
                         * - Browser prije POST/PATCH/DELETE zahtjeva može poslati OPTIONS zahtjev.
                         * - Bez ovog pravila frontend na Renderu može dobiti CORS error ili 403 preflight.
                         */
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        /**
                         * PROGRAMSKI ENTITET: endpoint pravilo
                         *
                         * SVRHA:
                         * - Endpointi za registraciju i prijavu moraju biti javno dostupni.
                         * - Korisnik se mora moći registrirati i prijaviti bez postojećeg JWT tokena.
                         */
                        .requestMatchers("/api/auth/**").permitAll()

                        /**
                         * PROGRAMSKI ENTITET: endpoint pravilo
                         *
                         * SVRHA:
                         * - /error endpoint se dopušta kako se ne bi dodatno komplicirala obrada grešaka.
                         */
                        .requestMatchers("/error").permitAll()

                        /**
                         * PROGRAMSKI ENTITET: endpoint pravilo
                         *
                         * SVRHA:
                         * - Admin endpointi dostupni su samo korisnicima s ADMIN rolom.
                         * - U JWT tokenu korisnik mora imati odgovarajuću rolu.
                         */
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        /**
                         * PROGRAMSKI ENTITET: završno sigurnosno pravilo
                         *
                         * SVRHA:
                         * - Svi ostali endpointi zahtijevaju autentifikaciju.
                         * - Korisnik mora poslati valjani JWT Bearer token.
                         */
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * PROGRAMSKI ENTITET: Bean metoda
     * TIP: CorsConfigurationSource
     *
     * SVRHA:
     * - Definira CORS pravila za backend API.
     * - Omogućuje komunikaciju između React frontenda i Spring Boot backenda.
     * - Dopušta lokalni frontend za razvoj i deployed frontend na Renderu.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        /**
         * PROGRAMSKI ENTITET: lista dopuštenih origin adresa
         *
         * SVRHA:
         * - Dopušta zahtjeve s lokalnog React development servera.
         * - Dopušta zahtjeve s React aplikacije deployane na Renderu.
         */
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://wasteapp-frontend.onrender.com"
        ));

        /**
         * PROGRAMSKI ENTITET: lista dopuštenih HTTP metoda
         *
         * SVRHA:
         * - Omogućuje standardne REST operacije prema backend API-ju.
         * - OPTIONS metoda je potrebna za CORS preflight zahtjeve.
         */
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        /**
         * PROGRAMSKI ENTITET: lista dopuštenih HTTP headera
         *
         * SVRHA:
         * - Dopušta slanje Content-Type headera za JSON zahtjeve.
         * - Dopušta slanje Authorization headera za JWT Bearer token.
         */
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        /**
         * PROGRAMSKI ENTITET: lista izloženih HTTP headera
         *
         * SVRHA:
         * - Omogućuje frontendu čitanje određenih response headera.
         * - Authorization se koristi kod autentifikacije, a Content-Disposition može biti koristan kod PDF preuzimanja.
         */
        configuration.setExposedHeaders(List.of(
                "Authorization",
                "Content-Disposition"
        ));

        /**
         * PROGRAMSKI ENTITET: CORS credentials postavka
         *
         * SVRHA:
         * - Postavljeno je na false jer aplikacija koristi JWT Bearer token.
         * - Autentifikacija se ne temelji na browser cookies/session mehanizmu.
         */
        configuration.setAllowCredentials(false);

        /**
         * PROGRAMSKI ENTITET: CORS source objekt
         *
         * SVRHA:
         * - Registrira CORS konfiguraciju za sve backend endpointove.
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}