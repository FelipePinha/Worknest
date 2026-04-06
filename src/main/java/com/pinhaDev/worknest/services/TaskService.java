package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.config.auth.JWTUserData;
import com.pinhaDev.worknest.domain.enums.TaskStatus;
import com.pinhaDev.worknest.domain.enums.UserRole;
import com.pinhaDev.worknest.domain.models.Task;
import com.pinhaDev.worknest.domain.models.User;
import com.pinhaDev.worknest.dto.request.CreateTaskRequest;
import com.pinhaDev.worknest.dto.request.UpdateTaskRequest;
import com.pinhaDev.worknest.dto.request.UpdateTaskStatusRequest;
import com.pinhaDev.worknest.dto.response.TaskResponse;
import com.pinhaDev.worknest.exception.AuthenticationException;
import com.pinhaDev.worknest.exception.ResourceNotFoundException;
import com.pinhaDev.worknest.exception.UnauthorizedException;
import com.pinhaDev.worknest.repositories.TaskRepository;
import com.pinhaDev.worknest.repositories.UserRepository;
import com.pinhaDev.worknest.repositories.WorkspaceMemberRepository;
import com.pinhaDev.worknest.repositories.WorkspaceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;


    public TaskService(TaskRepository taskRepository, UserRepository userRepository, WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    @Transactional
    public TaskResponse createTask(UUID workspaceId, CreateTaskRequest request) {
        var user = getLoggedUser();
        var workspace = workspaceRepository.findById(workspaceId)
                        .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrada"));

        validateOwner(user.getId(), workspaceId);

        var task = taskRepository.save(
                        Task.builder()
                            .title(request.title())
                            .description(request.description())
                            .status(TaskStatus.TODO)
                            .workspace(workspace)
                            .build()
        );

        return new TaskResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID workspaceId, UUID taskId, UpdateTaskRequest request) {
        var user = getLoggedUser();
        validateOwner(user.getId(), workspaceId);

        var task = taskRepository.findByWorkspaceIdAndId(workspaceId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task não existe nesta workspace"));

        if(request.title() != null) {
            task.setTitle(request.title());
        }

        if(request.description() != null) {
            task.setDescription(request.description());
        }

        taskRepository.save(task);

        return new TaskResponse(task);
    }

    @Transactional
    public void deleteTask(UUID workspaceId, UUID taskId) {
        var user = getLoggedUser();
        validateOwner(user.getId(), workspaceId);

        var task = taskRepository.findByWorkspaceIdAndId(workspaceId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task não existe nesta workspace"));

        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponse updateTaskStatus(UUID workspaceId, UUID taskId, UpdateTaskStatusRequest request) {
        var user = getLoggedUser();
        userExistsInWorkspace(user.getId(), workspaceId);

        var task = taskRepository.findByWorkspaceIdAndId(workspaceId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task não existe nesta workspace"));

        task.setStatus(request.status());

        return new TaskResponse(taskRepository.save(task));
    }

    private void validateOwner(UUID userId, UUID workspaceId) {
        workspaceMemberRepository.findByUserIdAndWorkspaceIdAndRole(userId, workspaceId, UserRole.OWNER)
                .orElseThrow(() -> new UnauthorizedException("Sem permissão"));
    }

    private void userExistsInWorkspace(UUID userId, UUID workspaceId) {
        workspaceMemberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não faz parte desta workspace"));
    }

    private User getLoggedUser() {
        var userData = (JWTUserData) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userData.email()).orElseThrow(() ->
                new AuthenticationException("Usuário autenticado não encontrado"));
    }
}
