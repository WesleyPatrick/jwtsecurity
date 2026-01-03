package com.wesleypatrick.jwtsecurity.controller.dto;

public record LoginResponse(
        String token,
        long expiresIn
) {
}
