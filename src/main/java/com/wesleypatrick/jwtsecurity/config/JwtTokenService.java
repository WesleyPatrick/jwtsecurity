package com.wesleypatrick.jwtsecurity.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wesleypatrick.jwtsecurity.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenService {

    private JwtProps props;
    private Algorithm algorithm;

    public JwtTokenService(JwtProps props, Algorithm algorithm) {
        this.props = props;
        this.algorithm = algorithm;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();
        Instant expiresAt = now.plus(props.accessMinutes(), ChronoUnit.MINUTES);

        return JWT.create()
                .withClaim("userId", String.valueOf(user.getId()))
                .withIssuer(props.issuer())
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .sign(algorithm);
    }
}
