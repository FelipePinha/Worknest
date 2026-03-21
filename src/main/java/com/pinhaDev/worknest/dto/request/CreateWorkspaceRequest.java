package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkspaceRequest(
        @NotEmpty
        String name,

        @NotNull
        UUID userId
) {

}