package com.zosh.job.controller;


import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.ProjectResponse;
import com.zosh.job.payload.AddProjectRequest;
import com.zosh.job.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    ResponseEntity<ProjectResponse> addProject(
            @PathVariable("resumeId") Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddProjectRequest request
    ) throws Exception {
        return ResponseEntity.ok(projectService.addProject(resumeId, candidateId, request));
    }

    @GetMapping
    ResponseEntity<List<ProjectResponse>> getProjects(
            @PathVariable("resumeId") Long resumeId) {
        return ResponseEntity.ok(projectService.getAllProjects(resumeId));
    }

    @PutMapping("/{projectId}")
    ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long resumeId,
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddProjectRequest request
    ) throws Exception {
        return ResponseEntity.ok(projectService.updateProject(projectId, resumeId, candidateId, request));
    }

    @DeleteMapping("/{projectId}")
    ResponseEntity<ApiResponse> deleteProject(
            @PathVariable Long resumeId,
            @PathVariable Long projectId,
            @RequestHeader("X-User-Id") Long candidateId
    ) throws Exception {
        projectService.deleteProject(projectId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Project Deleted Successfully", true));
    }
}
