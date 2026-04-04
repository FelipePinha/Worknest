package com.pinhaDev.worknest.dto.response;

import com.pinhaDev.worknest.domain.enums.TaskStatus;
import com.pinhaDev.worknest.domain.models.Task;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public TaskResponse(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
