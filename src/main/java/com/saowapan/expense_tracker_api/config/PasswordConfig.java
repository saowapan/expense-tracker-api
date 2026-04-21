package com.saowapan.expense_tracker_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder lives in its own config to break the circular dependency
 * between SecurityConfig → JwtAuthFilter → AppUserDetailsService → PasswordEncoder.
 *
 * This is a well-known Spring Security pattern when using custom UserDetailsService.
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}