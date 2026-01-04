package com.wesleypatrick.jwtsecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Tag(
        name = "Test controller",
        description = """
                Endpoints de teste para validar autorização por role.
                """
)
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Teste para USER",
            description = """
                    Acesso permitido somente para:
                    - ROLE_USER
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente/inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (role insuficiente)")
    })
    public String testUser() {
        return "User test";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Teste para ADMIN",
            description = """
                    Acesso permitido somente para:
                    - ROLE_ADMIN
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente/inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (role insuficiente)")
    })
    public String testAdmin() {
        return "admin test";
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Teste para qualquer usuário autenticado",
            description = """
                    Acesso permitido para qualquer usuário autenticado, por exemplo:
                    - ROLE_USER
                    - ROLE_ADMIN
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente/inválido)")
    })
    public String anyRoleTest() {
        return "any role test";
    }

    @GetMapping("/whoami")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Dados do usuário logado",
            description = """
                    Retorna informações do usuário autenticado (principal/email) e authorities.
                    
                    Acesso permitido para qualquer usuário autenticado:
                    - ROLE_USER
                    - ROLE_ADMIN
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Não autenticado (token ausente/inválido)")
    })
    public Object whoami(Authentication auth) {
        return Map.of(
                "email", auth.getName(),
                "authorities", auth.getAuthorities()
        );
    }
}
