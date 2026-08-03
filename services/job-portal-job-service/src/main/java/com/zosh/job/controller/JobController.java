package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.JobRequest;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.payload.JobSearchRequest;
import com.zosh.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@RequestHeader("X-User-Id") Long employerId,
                                                 @RequestBody @Valid JobRequest jobRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(employerId, jobRequest));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId) throws Exception {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs(@ModelAttribute JobSearchRequest request) {
        return ResponseEntity.ok(jobService.getJobs(request));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobResponse>> getJobsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<JobResponse>> getAllJobsAdmin() {
        return ResponseEntity.ok(jobService.getAllJobsAdmin());
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId,
                                                 @RequestHeader("X-User-Id") Long employerId,
                                                 @RequestBody @Valid JobRequest jobRequest) throws Exception {
        return ResponseEntity.ok(jobService.updateJob(jobId, employerId, jobRequest));
    }

    @PatchMapping("/{jobId}/publish")
    public ResponseEntity<JobResponse> publishJob(@PathVariable Long jobId,
                                                  @RequestHeader("X-User-Id") Long employerId) throws Exception {
        return ResponseEntity.ok(jobService.publishJob(jobId, employerId));
    }

    @PatchMapping("/{jobId}/close")
    public ResponseEntity<JobResponse> closeJob(@PathVariable Long jobId,
                                                @RequestHeader("X-User-Id") Long employerId) throws Exception {
        return ResponseEntity.ok(jobService.closeJob(jobId, employerId));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable Long jobId,
                                                 @RequestHeader("X-User-Id") Long employerId) throws Exception {
        jobService.deleteJob(jobId, employerId);
        return ResponseEntity.ok(new ApiResponse("Job deleted successfully", true));
    }
}
