package com.wesleypatrick.jwtsecurity.security.refresh;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.wesleypatrick.jwtsecurity.model.RefreshToken;
import com.wesleypatrick.jwtsecurity.model.User;
import com.wesleypatrick.jwtsecurity.repository.RefreshTokenRepository;
import com.wesleypatrick.jwtsecurity.repository.UserRepository;
import com.wesleypatrick.jwtsecurity.security.utils.HashUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public RefreshToken upsert(User user, String token) {
        String lookup = HashUtils.sha256Hex(token);
        String hashed = passwordEncoder.encode(lookup);

        RefreshToken rft = refreshTokenRepository.findByUser(user).orElseGet(RefreshToken::new);

        rft.setUser(user);
        rft.setLookupHash(lookup);
        rft.setHashedToken(hashed);

        refreshTokenRepository.save(rft);
        return rft;

    }

    @Transactional
    public User getUserFromRefresh(String refreshJwt) {
        RefreshToken stored = validateAndGet(refreshJwt);

        UUID userId = stored.getUser().getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public RefreshToken validateAndGet(String token) {
        String lookup = HashUtils.sha256Hex(token);

        RefreshToken storagedToken = refreshTokenRepository.findByLookupHash(lookup).orElseThrow(
                () -> new RuntimeException("Refresh token inválido")
        );

        if(!passwordEncoder.matches(lookup, storagedToken.getHashedToken())) {
            throw new RuntimeException("Refresh token inválido");
        }

        return storagedToken;
    }

    @Transactional
    public void revoke(String email) {
        System.out.println(email + " revocado");
        refreshTokenRepository.deleteByUser_Email(email);
    }
}
