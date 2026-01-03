package com.wesleypatrick.jwtsecurity.config;

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
}