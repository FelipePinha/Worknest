package com.pinhaDev.worknest.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256(secret);

            DecodedJWT decode = JWT.require(algorithm)
                    .build().verify(token);

            return Optional.of(JWTUserData.builder().email(decode.getSubject()).build());
        } catch (com.auth0.jwt.exceptions.JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
