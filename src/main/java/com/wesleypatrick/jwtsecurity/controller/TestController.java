package com.wesleypatrick.jwtsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String test() {
        return "User test";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String testAdmin() {
        return "admin test";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String anyRoleTest() {
        return "any role test";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/whoami")
    public Object whoami(Authentication auth) {
        return Map.of(
                "email", auth.getPrincipal(),
                "authorities", auth.getAuthorities()
        );
    }


}
