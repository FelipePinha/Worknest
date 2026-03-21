package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record RegisterRequest(
        @NotEmpty
        @Email
        String email,

        @NotEmpty
        @Min(8)
        String password,

        @NotEmpty
        @Min(8)
        String passwordConfirmation
) {
}
