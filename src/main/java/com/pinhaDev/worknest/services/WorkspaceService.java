package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.config.JWTUserData;
import com.pinhaDev.worknest.domain.enums.UserRole;
import com.pinhaDev.worknest.domain.models.User;
import com.pinhaDev.worknest.domain.models.WorkspaceMember;
import com.pinhaDev.worknest.dto.request.AddContributorRequest;
import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.request.UpdateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.AddContributorResponse;
import com.pinhaDev.worknest.dto.response.WorkspaceResponse;
import com.pinhaDev.worknest.domain.models.Workspace;
import com.pinhaDev.worknest.repositories.UserRepository;
import com.pinhaDev.worknest.repositories.WorkspaceMemberRepository;
import com.pinhaDev.worknest.repositories.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Workspace workspace = Workspace.builder().name(request.name()).build();

        var user = getLoggedUser();

       var newWorkspace = workspaceRepository.save(workspace);
       var workspaceMember = new WorkspaceMember(user, newWorkspace);

       workspaceMemberRepository.save(workspaceMember);

       return new WorkspaceResponse(newWorkspace);
    }

    @Transactional
    public AddContributorResponse addContributor(AddContributorRequest request, UUID workspaceId) {
        var user = getLoggedUser();
        var workspace = workspaceRepository.findById(workspaceId).orElseThrow(() ->
                new IllegalArgumentException("Workspace não encontrado"));

        var contributor = userRepository.findByEmail(request.contributorEmail()).orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado"));

        validateOwner(user.getId(), workspaceId);

        var contributorIsOwner = workspaceMemberRepository.findByUserIdAndWorkspaceIdAndRole(
                contributor.getId(), workspaceId, UserRole.OWNER).isPresent();

        if(contributorIsOwner) {
            throw new IllegalArgumentException("Não permitido adicionar o dono como colaborador");
        }

        var alreadyContributor = workspaceMemberRepository.findByUserIdAndWorkspaceId(contributor.getId(), workspaceId)
                .isPresent();

        if(alreadyContributor) {
            throw new IllegalArgumentException("Usuário já é um colaborador");
        }

        var newWorkspaceMember = WorkspaceMember
                .builder()
                .user(contributor)
                .workspace(workspace)
                .role(UserRole.CONTRIBUTOR)
                .build();

        var workspaceMember = workspaceMemberRepository.save(newWorkspaceMember);

        return new AddContributorResponse(
                workspaceMember.getId(),
                request.contributorEmail(),
                workspaceMember.getRole(),
                workspaceId
        );
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(UpdateWorkspaceRequest request, UUID WorkspaceId) {
        var user = getLoggedUser();

        var workspace = workspaceRepository.findById(WorkspaceId)
                .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrado"));

        validateOwner(user.getId(), workspace.getId());

        if (request.name() != null) {
            workspace.setName(request.name());
        }

        workspaceRepository.save(workspace);

        return new WorkspaceResponse(workspace);
    }

    @Transactional
    public void deleteWorkspace(UUID workspaceId) {
        var user = getLoggedUser();

        validateOwner(user.getId(), workspaceId);

        workspaceRepository.deleteById(workspaceId);
    }

    public Page<WorkspaceResponse> getUserOwnedWorkspaces(Pageable pageable) {
        var user = getLoggedUser();

        return workspaceMemberRepository.findWorkspacesByUserIdAndRole(user.getId(), UserRole.OWNER, pageable)
                .map(WorkspaceResponse::new);
    }

    public Page<WorkspaceResponse> getUserContributorWorkspaces(Pageable pageable) {
        var user = getLoggedUser();

        return workspaceMemberRepository.findWorkspacesByUserIdAndRole(user.getId(), UserRole.CONTRIBUTOR, pageable)
                .map(WorkspaceResponse::new);
    }

    public WorkspaceResponse getUserWorkspace(UUID workspaceId) {
        var user = getLoggedUser();

        WorkspaceMember member = workspaceMemberRepository.findByUserIdAndWorkspaceId(
                        user.getId(),
                        workspaceId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Workspace não encontrada ou você não é membro"));

        return new WorkspaceResponse(member.getWorkspace());
    }

    private void validateOwner(UUID userId, UUID workspaceId) {
        workspaceMemberRepository.findByUserIdAndWorkspaceIdAndRole(userId, workspaceId, UserRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("Sem permissão"));
    }

    private User getLoggedUser() {
        var userData = (JWTUserData) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userData.email()).orElseThrow(() ->
                new IllegalArgumentException("Usuário autenticado não encontrado"));
    }
}
