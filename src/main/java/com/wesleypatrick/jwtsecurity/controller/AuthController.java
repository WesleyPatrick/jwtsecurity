package com.wesleypatrick.jwtsecurity.controller;
import com.wesleypatrick.jwtsecurity.config.JwtProps;
import com.wesleypatrick.jwtsecurity.config.JwtTokenService;
import com.wesleypatrick.jwtsecurity.controller.dto.CreateUserDto;
import com.wesleypatrick.jwtsecurity.controller.dto.LoginRequestDto;
import com.wesleypatrick.jwtsecurity.controller.dto.LoginResponse;
import com.wesleypatrick.jwtsecurity.controller.dto.UserResponse;
import com.wesleypatrick.jwtsecurity.mapper.UserMapper;
import com.wesleypatrick.jwtsecurity.model.Role;
import com.wesleypatrick.jwtsecurity.model.User;
import com.wesleypatrick.jwtsecurity.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final long SIXTY_SECONDS = 60;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final JwtProps jwtProps;

    public AuthController(UserRepository userRepository, UserMapper userMapper,
                          PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                          JwtTokenService jwtTokenService, JwtProps jwtProps) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.jwtProps = jwtProps;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto req) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                req.email(),
                req.password()
        );
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) auth.getPrincipal();
        String token = jwtTokenService.generateToken(user);

        System.out.println(user);

        return ResponseEntity.ok(new LoginResponse(token, jwtProps.accessMinutes() * SIXTY_SECONDS));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserDto req) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setName(req.name());
        user.setRole(Role.USER);

        userRepository.save(user);

        UserResponse userResponse = userMapper.toUserResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
