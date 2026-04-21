package com.saowapan.expense_tracker_api.controller;

import com.saowapan.expense_tracker_api.dto.LoginRequest;
import com.saowapan.expense_tracker_api.dto.LoginResponse;
import com.saowapan.expense_tracker_api.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(request.username());
        long expiresAt = new Date().getTime() / 1000 + 3600; // 1 hour from now, in seconds

        return ResponseEntity.ok(new LoginResponse(token, request.username(), expiresAt));
    }
}