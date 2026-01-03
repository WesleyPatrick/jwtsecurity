package com.wesleypatrick.jwtsecurity.controller.dto;

public record ErrorResponse(
        int status,
        String message
) {
}
