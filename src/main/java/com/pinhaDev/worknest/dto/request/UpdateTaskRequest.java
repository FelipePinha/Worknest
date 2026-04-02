package com.pinhaDev.worknest.dto.request;

public record UpdateTaskRequest(
        String title,
        String description
) {
}
