package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.payload.CompanyApplicationFilterRequest;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.payload.UpdateApplicationStatusRequest;
import com.zosh.job.payload.WithdrawApplicationRequest;
import com.zosh.job.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService service;

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid CreateApplicationRequest request) throws Exception {
        return ResponseEntity.ok(service.createApplication(candidateId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(
            @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(service.getApplicationById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @RequestHeader("X-User-Id") Long candidateId) {
        return ResponseEntity.ok(service.getApplications(candidateId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(service.getApplicationsForJob(jobId));
    }

    @GetMapping("/company")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForCompany(
            @RequestHeader("X-User-Id") Long userId,
            @ModelAttribute CompanyApplicationFilterRequest filter) {
        return ResponseEntity.ok(service.getApplicationsForCompany(userId, filter));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid UpdateApplicationStatusRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.updateStatus(id, employerId, request.getStatus()));
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid WithdrawApplicationRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.withdraw(id, candidateId, request));
    }


    @PatchMapping("/{id}/star")
    public ResponseEntity<ApplicationResponse> toggleStar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long employerId
    ) throws Exception {
        return ResponseEntity.ok(service.toggleStar(id, employerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteApplication(@PathVariable Long id,
                                                         @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        service.deleteApplication(id, candidateId);
        return ResponseEntity.ok(new ApiResponse("Application Deleted Successfully", true));
    }

}
