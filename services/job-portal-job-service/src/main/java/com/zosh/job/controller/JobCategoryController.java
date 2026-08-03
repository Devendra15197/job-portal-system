package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.payload.JobCategoryRequest;
import com.zosh.job.service.JobCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
@RequiredArgsConstructor
public class JobCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(JobCategoryController.class);

    private final JobCategoryService jobCategoryService;

    @PostMapping
    public ResponseEntity<JobCategoryResponse> createJobCategory(@RequestBody @Valid JobCategoryRequest jobCategoryRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(jobCategoryService.createJobCategory(jobCategoryRequest));
        } catch (Exception e) {
            logger.error("Error creating job category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<JobCategoryResponse>> getAllJobCategories() {
        try {
            return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
        } catch (Exception e) {
            logger.error("Error getting all job categories: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> getJobCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobCategoryService.getJobCategoryById(id));
        } catch (Exception e) {
            logger.error("Error getting  job categories by Id: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCategoryResponse> updateJobCategory(@PathVariable Long id,
                                                                 @RequestBody JobCategoryRequest jobCategoryRequest) {
        try {
            return ResponseEntity.ok(jobCategoryService.updateJobCategory(id, jobCategoryRequest));
        } catch (Exception e) {
            logger.error("Error updating job category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJobCategory(@PathVariable Long id) {
        try {
            jobCategoryService.deleteJobCategory(id);
            return ResponseEntity.ok(new ApiResponse("Job category deleted successfully", true));
        } catch (Exception e) {
            logger.error("Error deleting job category: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
