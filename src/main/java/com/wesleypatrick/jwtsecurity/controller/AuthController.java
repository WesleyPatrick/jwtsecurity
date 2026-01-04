package com.wesleypatrick.jwtsecurity.controller;
import com.wesleypatrick.jwtsecurity.config.properties.JwtProps;
import com.wesleypatrick.jwtsecurity.controller.dto.*;
import com.wesleypatrick.jwtsecurity.security.jwt.JwtTokenService;
import com.wesleypatrick.jwtsecurity.mapper.UserMapper;
import com.wesleypatrick.jwtsecurity.model.Role;
import com.wesleypatrick.jwtsecurity.model.User;
import com.wesleypatrick.jwtsecurity.repository.UserRepository;
import com.wesleypatrick.jwtsecurity.security.refresh.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Autenticação e gestão de tokens JWT")
public class AuthController {

    private static final long SIXTY_SECONDS = 60;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final JwtProps jwtProps;
    private final RefreshTokenService refreshTokenService;


    public AuthController(UserRepository userRepository, UserMapper userMapper,
                          PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                          JwtTokenService jwtTokenService, JwtProps jwtProps,  RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.jwtProps = jwtProps;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content())
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto req) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                req.email(),
                req.password()
        );
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) auth.getPrincipal();
        String token = jwtTokenService.generateToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        refreshTokenService.upsert(user, refreshToken);

        return ResponseEntity.ok(new LoginResponse(token, jwtProps.accessMinutes() * SIXTY_SECONDS,  refreshToken));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Cadastro de novo usuário",
            description = "Cria um novo usuário com perfil USER"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content()
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserDto req) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setName(req.name());
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);

        UserResponse userResponse = userMapper.toUserResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Gera novo access token usando refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido", content = @Content())
    })
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest req) {
        var decoded = jwtTokenService.verifyToken(req.refreshToken());

        if (!jwtTokenService.isRefreshToken(decoded)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var stored = refreshTokenService.validateAndGet(req.refreshToken());

        User user = refreshTokenService.getUserFromRefresh(req.refreshToken());

        String newAccessToken = jwtTokenService.generateToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);

        refreshTokenService.upsert(user, newAccessToken);

        return ResponseEntity.ok(new LoginResponse(newAccessToken, jwtProps.accessMinutes() * SIXTY_SECONDS,  newRefreshToken));
    }


    @PostMapping("/logout")
    @Operation(summary = "Logout do usuário (revoga refresh token)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout realizado", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content())
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> logout(Authentication auth) {
        String email = auth.getName();
        refreshTokenService.revoke(email);
        return ResponseEntity.noContent().build();
    }



}
