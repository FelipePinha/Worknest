package com.pinhaDev.worknest.services;

import com.pinhaDev.worknest.config.JWTUserData;
import com.pinhaDev.worknest.domain.enums.TaskStatus;
import com.pinhaDev.worknest.domain.enums.UserRole;
import com.pinhaDev.worknest.domain.models.Task;
import com.pinhaDev.worknest.domain.models.User;
import com.pinhaDev.worknest.dto.request.CreateTaskRequest;
import com.pinhaDev.worknest.dto.request.UpdateTaskRequest;
import com.pinhaDev.worknest.repositories.TaskRepository;
import com.pinhaDev.worknest.repositories.UserRepository;
import com.pinhaDev.worknest.repositories.WorkspaceMemberRepository;
import com.pinhaDev.worknest.repositories.WorkspaceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public Task createTask(UUID workspaceId, CreateTaskRequest request) {
        var user = getLoggedUser();
        var workspace = workspaceRepository.findById(workspaceId)
                        .orElseThrow(() -> new IllegalArgumentException("Workspace não encontrada"));

        validateOwner(user.getId(), workspaceId);

        return taskRepository.save(
                Task.builder()
                        .title(request.title())
                        .description(request.description())
                        .status(TaskStatus.TODO)
                        .workspace(workspace)
                        .build()
        );
    }

    public Task updateTask(UUID workspaceId, UUID taskId, UpdateTaskRequest request) {
        var user = getLoggedUser();
        validateOwner(user.getId(), workspaceId);

        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task não encontrada"));

        if(request.title() != null) {
            task.setTitle(request.title());
        }

        if(request.description() != null) {
            task.setDescription(request.description());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(UUID workspaceId, UUID taskId) {
        var user = getLoggedUser();
        validateOwner(user.getId(), workspaceId);

        taskRepository.deleteById(taskId);
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
