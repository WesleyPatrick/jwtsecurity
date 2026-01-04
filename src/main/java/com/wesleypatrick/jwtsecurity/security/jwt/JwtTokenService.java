package com.wesleypatrick.jwtsecurity.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wesleypatrick.jwtsecurity.config.properties.JwtProps;
import com.wesleypatrick.jwtsecurity.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenService {

    private JwtProps props;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtTokenService(JwtProps props, Algorithm algorithm,  JWTVerifier verifier) {
        this.props = props;
        this.algorithm = algorithm;
        this.verifier = verifier;
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

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(props.refreshDays(), ChronoUnit.DAYS);

        return JWT.create()
                .withIssuer(props.issuer())
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim("userId", user.getId().toString())
                .withClaim("type", "refresh")
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
            return verifier.verify(token);
    }

    public boolean isRefreshToken(DecodedJWT jwt) {
        return "refresh".equals(jwt.getClaim("type").asString());
    }
}
