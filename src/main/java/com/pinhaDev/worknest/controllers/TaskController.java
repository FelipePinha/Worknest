package com.pinhaDev.worknest.controllers;

import com.pinhaDev.worknest.domain.models.Task;
import com.pinhaDev.worknest.dto.request.CreateTaskRequest;
import com.pinhaDev.worknest.dto.request.UpdateTaskRequest;
import com.pinhaDev.worknest.dto.request.UpdateTaskStatusRequest;
import com.pinhaDev.worknest.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspace")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{workspaceId}/tasks")
    public ResponseEntity<Task> createTask(
            @PathVariable UUID workspaceId,
            @Valid @RequestBody CreateTaskRequest request)
    {
        var task = taskService.createTask(workspaceId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{workspaceId}/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable UUID workspaceId,
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        var task = taskService.updateTask(workspaceId, taskId, request);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{workspaceId}/tasks/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable UUID workspaceId,
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        var task = taskService.updateTaskStatus(workspaceId, taskId, request);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{workspaceId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID workspaceId, @PathVariable UUID taskId) {
        taskService.deleteTask(workspaceId, taskId);

        return ResponseEntity.noContent().build();
    }
}
