package com.saowapan.expense_tracker_api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * In-memory user store for the MVP. Credentials come from environment variables
 * so they're never committed to source. Will be replaced with a JPA-backed
 * implementation when multi-user support is added.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final Map<String, String> users;

    public AppUserDetailsService(
            @Value("${app.admin.username}") String adminUsername,
            @Value("${app.admin.password}") String adminPassword,
            PasswordEncoder passwordEncoder
    ) {
        this.users = Map.of(
                adminUsername, passwordEncoder.encode(adminPassword)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String hashedPassword = users.get(username);
        if (hashedPassword == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.withUsername(username)
                .password(hashedPassword)
                .roles("USER")
                .build();
    }
}