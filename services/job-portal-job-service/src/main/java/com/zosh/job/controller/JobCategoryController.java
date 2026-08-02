package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.payload.JobCategoryRequest;
import com.zosh.job.repository.JobCategoryRepository;
import com.zosh.job.service.JobCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
@RequiredArgsConstructor
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    @PostMapping
    public ResponseEntity<JobCategoryResponse> createJobCategory(JobCategoryRequest jobCategoryRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(jobCategoryService.createJobCategory(jobCategoryRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<JobCategoryResponse>> getAllJobCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> getJobCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobCategoryService.getJobCategoryById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> updateJobCategory(@PathVariable Long id,
                                                                 @RequestBody JobCategoryRequest jobCategoryRequest) {
        try {
            return ResponseEntity.ok(jobCategoryService.updateJobCategory(id, jobCategoryRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJobCategory(@PathVariable Long id) {
        try {
            jobCategoryService.deleteJobCategory(id);
            return ResponseEntity.ok(new ApiResponse("Job category deleted successfully", true));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
