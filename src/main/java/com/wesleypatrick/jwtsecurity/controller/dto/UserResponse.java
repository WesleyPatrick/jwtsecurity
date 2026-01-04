package com.wesleypatrick.jwtsecurity.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserResponse(

        @Schema(example = "b5a0b1e4-0b41-4f67-8a0d-9a5b0a2f1c3d")
        UUID id,

        @Schema(example = "Wesley Patrick")
        String name,

        @Schema(example = "wesley@email.com")
        String email
) {
}
