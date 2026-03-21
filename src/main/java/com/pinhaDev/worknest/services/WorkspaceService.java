package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.domain.models.WorkspaceMember;
import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.WorkspaceResponse;
import com.pinhaDev.worknest.domain.models.Workspace;
import com.pinhaDev.worknest.repositories.UserRepository;
import com.pinhaDev.worknest.repositories.WorkspaceMemberRepository;
import com.pinhaDev.worknest.repositories.WorkspaceRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
       var workspaceMember = new WorkspaceMember(user, workspace);

       workspaceMemberRepository.save(workspaceMember);

        return new WorkspaceResponse(newWorkspace);
    }
}
