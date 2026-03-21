package com.pinhaDev.worknest.controllers;

import com.pinhaDev.worknest.dto.request.CreateWorkspaceRequest;
import com.pinhaDev.worknest.dto.response.WorkspaceResponse;
import com.pinhaDev.worknest.services.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request) {
        var workspace = workspaceService.createWorkspace(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
    }
}
