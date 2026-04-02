package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateTaskRequest(
        @NotEmpty
        String title,

        @NotEmpty
        String description
) {
}
