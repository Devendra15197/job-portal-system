package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.JobTagResponse;
import com.zosh.job.payload.JobTagRequest;
import com.zosh.job.repository.JobTagRepository;
import com.zosh.job.service.JobService;
import com.zosh.job.service.JobTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-tags")
public class JobTagController {

    private final JobTagService jobTagService;

    @PostMapping
    ResponseEntity<JobTagResponse> createJobTag(@RequestBody @Valid JobTagRequest jobTagRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobTagService.createJobTag(jobTagRequest));
    }


    @GetMapping
    ResponseEntity<List<JobTagResponse>> getAllJobTags() {
        return ResponseEntity.ok(jobTagService.getAllJobTags());
    }

    @GetMapping("/{id}")
    ResponseEntity<JobTagResponse> getJobTagById(@PathVariable Long id) {
        return ResponseEntity.ok(jobTagService.getJobTagById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<JobTagResponse> updateJobTag(@PathVariable Long id,
                                                @RequestBody @Valid JobTagRequest jobTagRequest) {
        return ResponseEntity.ok(jobTagService.updateJobTag(id, jobTagRequest));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse> deleteJobTag(@PathVariable Long id) {
        jobTagService.deleteJobTag(id);
        return ResponseEntity.ok(new ApiResponse("Job tag deleted successfully", true));
    }
}
