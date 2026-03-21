package com.pinhaDev.worknest.config;

import lombok.Builder;

@Builder
public record JWTUserData(
    String email
) {
}
