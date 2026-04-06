package com.pinhaDev.worknest.config.auth;

import lombok.Builder;

@Builder
public record JWTUserData(
    String email
) {
}
