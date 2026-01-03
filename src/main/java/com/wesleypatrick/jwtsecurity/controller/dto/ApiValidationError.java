package com.wesleypatrick.jwtsecurity.controller.dto;

import java.util.List;

public record ApiValidationError(
        int status,
        String message,
        List<String> errors
) {
}
