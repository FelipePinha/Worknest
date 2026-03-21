package com.pinhaDev.worknest.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateWorkspaceRequest(
        @NotEmpty
        String name
) {}
