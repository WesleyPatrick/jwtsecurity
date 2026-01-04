package com.wesleypatrick.jwtsecurity.config;

import com.wesleypatrick.jwtsecurity.model.Role;
import com.wesleypatrick.jwtsecurity.model.User;
import com.wesleypatrick.jwtsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
    }

    private void seedAdmin() {
        var email = "admin@email.com";

        if (userRepository.existsByEmail(email)) return;

        var user = new User();
        user.setName("ADMIN");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(Role.ADMIN);
        user.setEnabled(true);

        userRepository.save(user);
    }
}
