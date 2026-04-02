package com.pinhaDev.worknest.dto.request;

import com.pinhaDev.worknest.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull
        TaskStatus status
) {
}
