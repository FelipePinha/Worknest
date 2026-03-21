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
    public WorkspaceResponse(Workspace workplace) {
        this(workplace.getId(), workplace.getName(), workplace.getCreatedAt(), workplace.getUpdatedAt());
    }
}
