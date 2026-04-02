package com.pinhaDev.worknest.repositories;

import com.pinhaDev.worknest.domain.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByWorkspaceIdAndId(UUID workspaceId, UUID id);
}
