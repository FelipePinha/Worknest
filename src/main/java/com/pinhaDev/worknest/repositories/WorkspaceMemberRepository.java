package com.pinhaDev.worknest.repositories;

import com.pinhaDev.worknest.domain.enums.UserRole;
import com.pinhaDev.worknest.domain.models.WorkspaceMember;
import com.pinhaDev.worknest.domain.models.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {

    @Query(value = """
            SELECT *
            FROM tb_workspace_members wm
            WHERE wm.user_id = :userId
            AND wm.workplace_id = :workspaceId
            AND wm.role = CAST(:#{#role.name()} AS user_role)
            """, nativeQuery = true)
    Optional<WorkspaceMember> findByUserIdAndWorkspaceIdAndRole(@Param("userId") UUID userId,
                                                 @Param("workspaceId") UUID workspaceId,
                                                 @Param("role") UserRole role);

    @Query(value = """
            SELECT w.*
            FROM tb_workspaces w
            JOIN tb_workspace_members wm ON w.id = wm.workplace_id
            WHERE wm.user_id = :userId
            AND wm.role = CAST(:#{#role.name()} AS user_role)
            """,
            nativeQuery = true)
    Page<Workspace> findWorkspacesByUserIdAndRole(@Param("userId") UUID userId, @Param("role") UserRole role, Pageable pageable);
}
