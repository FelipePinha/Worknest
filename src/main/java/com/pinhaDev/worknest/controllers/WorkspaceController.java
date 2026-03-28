package com.pinhaDev.worknest.controllers;

import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.request.UpdateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.WorkspaceResponse;
import com.pinhaDev.worknest.services.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request) {
        var workspace = workspaceService.createWorkspace(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
    }

    @GetMapping("/user/{userId}/owned")
    public ResponseEntity<Page<WorkspaceResponse>> getUserOwnedWorkspaces(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<WorkspaceResponse> workspaces = workspaceService.getUserOwnedWorkspaces(userId, pageable);
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/user/{userId}/contributor")
    public ResponseEntity<Page<WorkspaceResponse>> getUserContributorWorkspaces(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<WorkspaceResponse> workspaces = workspaceService.getUserContributorWorkspaces(userId, pageable);
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/{id}/user/{userId}/owned")
    public ResponseEntity<WorkspaceResponse> getUserOwnedWorkspace(
            @PathVariable UUID id,
            @PathVariable UUID userId
    ) {
        WorkspaceResponse workspace = workspaceService.getUserOwnedWorkspace(userId, id);
        return ResponseEntity.ok(workspace);
    }

    @GetMapping("/{id}/user/{userId}/contributor")
    public ResponseEntity<WorkspaceResponse> getUserContributorWorkspace(
            @PathVariable UUID id,
            @PathVariable UUID userId
    ) {
        WorkspaceResponse workspace = workspaceService.getUserContributorWorkspace(userId, id);
        return ResponseEntity.ok(workspace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @Valid @RequestBody UpdateWorkspaceRequest request,
            @PathVariable UUID id
    ) {
        WorkspaceResponse workspace = workspaceService.updateWorkspace(request, id);

        return ResponseEntity.ok().body(workspace);
    }
}
