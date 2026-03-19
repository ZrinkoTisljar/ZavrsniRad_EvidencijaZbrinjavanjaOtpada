package com.example.wasteapp.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PROGRAMSKI ENTITET: configuration class
 *
 * SVRHA:
 * - Definira PasswordEncoder bean za hashiranje lozinki.
 * - BCrypt je standard za spremanje lozinki (salt + adaptive cost).
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
