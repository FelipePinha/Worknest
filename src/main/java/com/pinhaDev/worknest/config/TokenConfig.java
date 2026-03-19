package com.pinhaDev.worknest.config;

import org.springframework.stereotype.Component;

@Component
public class TokenConfig {
    private final String secret = "${JWT_SECRET}";

    public String generateToken(String email) {
        try {
            com.auth0.jwt.algorithms.Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256(secret);
            return com.auth0.jwt.JWT.create()
                    .withIssuer("worknest")
                    .withSubject(email)
                    .withExpiresAt(java.time.Instant.now().plusSeconds(86400))
                    .sign(algorithm);
        } catch (com.auth0.jwt.exceptions.JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            com.auth0.jwt.algorithms.Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256(secret);
            return com.auth0.jwt.JWT.require(algorithm)
                    .withIssuer("worknest")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (com.auth0.jwt.exceptions.JWTVerificationException exception) {
            return "";
        }
    }
}
