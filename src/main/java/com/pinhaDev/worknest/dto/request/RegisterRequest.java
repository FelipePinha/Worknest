package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotEmpty
        @Email
        String email,

        @NotEmpty
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @NotEmpty
        @Size(min = 8)
        String passwordConfirmation
) {
}
