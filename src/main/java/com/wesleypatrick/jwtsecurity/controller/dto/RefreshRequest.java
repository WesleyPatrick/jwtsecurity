package com.wesleypatrick.jwtsecurity.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshRequest(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String refreshToken
) {
}
