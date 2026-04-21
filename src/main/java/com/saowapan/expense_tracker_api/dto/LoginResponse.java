package com.saowapan.expense_tracker_api.dto;

public record LoginResponse (
        String token,
        String username,
        long expiresAt
) {}
