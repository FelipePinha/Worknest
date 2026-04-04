package com.pinhaDev.worknest.controllers;

import com.pinhaDev.worknest.dto.request.AddContributorRequest;
import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.request.UpdateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.AddContributorResponse;
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
@RequestMapping("/workspaces")
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

    @PostMapping("/{id}/contributor")
    public ResponseEntity<AddContributorResponse> addContributor(@Valid @RequestBody AddContributorRequest request, @PathVariable UUID id) {
        var contributor = workspaceService.addContributor(request, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(contributor);
    }

    @GetMapping("/owned")
    public ResponseEntity<Page<WorkspaceResponse>> getUserOwnedWorkspaces(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<WorkspaceResponse> workspaces = workspaceService.getUserOwnedWorkspaces(pageable);
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/contributing")
    public ResponseEntity<Page<WorkspaceResponse>> getUserContributorWorkspaces(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<WorkspaceResponse> workspaces = workspaceService.getUserContributorWorkspaces(pageable);
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getUserOwnedWorkspace(
            @PathVariable UUID id
    ) {
        WorkspaceResponse workspace = workspaceService.getUserWorkspace(id);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
}
