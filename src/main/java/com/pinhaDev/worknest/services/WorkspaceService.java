package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.domain.enums.UserRole;
import com.pinhaDev.worknest.domain.models.WorkspaceMember;
import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.request.UpdateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.WorkspaceResponse;
import com.pinhaDev.worknest.domain.models.Workspace;
import com.pinhaDev.worknest.repositories.UserRepository;
import com.pinhaDev.worknest.repositories.WorkspaceMemberRepository;
import com.pinhaDev.worknest.repositories.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {
        Workspace workspace = new Workspace(request.name());

        var user = userRepository.findById(request.userId()).orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado"));

       var newWorkspace = workspaceRepository.save(workspace);
       var workspaceMember = new WorkspaceMember(user, newWorkspace);

       workspaceMemberRepository.save(workspaceMember);

       return new WorkspaceResponse(newWorkspace);
    }

    public WorkspaceResponse updateWorkspace(UpdateWorkspaceRequest request, UUID id) {
        var workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrado"));

        if (request.name() != null) {
            workspace.setName(request.name());
        }

        workspaceRepository.save(workspace);

        return new WorkspaceResponse(workspace);
    }

    public Page<WorkspaceResponse> getUserOwnedWorkspaces(UUID userId, Pageable pageable) {
        return workspaceMemberRepository.findWorkspacesByUserIdAndRole(userId, UserRole.OWNER, pageable)
                .map(WorkspaceResponse::new);
    }

    public Page<WorkspaceResponse> getUserContributorWorkspaces(UUID userId, Pageable pageable) {
        return workspaceMemberRepository.findWorkspacesByUserIdAndRole(userId, UserRole.CONTRIBUTOR, pageable)
                .map(WorkspaceResponse::new);
    }

    public WorkspaceResponse getUserOwnedWorkspace(UUID userId, UUID workspaceId) {
        WorkspaceMember member = workspaceMemberRepository.findByUserIdAndWorkspaceIdAndRole(userId, workspaceId, UserRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrada ou você não tem permissão de proprietário"));

        return new WorkspaceResponse(member.getWorkspace());
    }

    public WorkspaceResponse getUserContributorWorkspace(UUID userId, UUID workspaceId) {
        WorkspaceMember member = workspaceMemberRepository.findByUserIdAndWorkspaceIdAndRole(userId, workspaceId, UserRole.CONTRIBUTOR)
                .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrada"));

        return new WorkspaceResponse(member.getWorkspace());
    }
}
