package com.wesleypatrick.jwtsecurity.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProps.class)
public class TokenConfig {

    @Bean
    Algorithm jwtAlgorithm(JwtProps props) {
        return Algorithm.HMAC256(props.secret());
    }

    @Bean
    JWTVerifier jwtVerifier(JwtProps props, Algorithm algorithm) {
        return JWT.require(algorithm)
                .withIssuer(props.issuer())
                .build();
    }
}