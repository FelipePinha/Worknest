package com.pinhaDev.worknest.dto.response;

import com.pinhaDev.worknest.domain.models.Workspace;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public WorkspaceResponse(Workspace workspace) {
        this(workspace.getId(), workspace.getName(), workspace.getCreatedAt(), workspace.getUpdatedAt());
    }
}
