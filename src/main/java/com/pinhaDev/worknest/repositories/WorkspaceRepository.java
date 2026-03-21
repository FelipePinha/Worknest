package com.pinhaDev.worknest.repositories;

import com.pinhaDev.worknest.domain.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
