package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.SavedJobResponse;
import com.zosh.job.payload.SaveJobRequest;
import com.zosh.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences/saved-jobs")
public class SavedJobController {
    private final SavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<SavedJobResponse> saveJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody SaveJobRequest savedJobRequest
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJobService.saveJob(candidateId, savedJobRequest));
    }

    @GetMapping
    public ResponseEntity<List<SavedJobResponse>> getSavedJobs(
            @RequestHeader("X-User-Id") Long candidateId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(savedJobService.getSavedJob(candidateId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkSavedJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam Long jobId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(savedJobService.isSaved(candidateId, jobId));
    }

    @DeleteMapping("{/savedJobId}")
    public ResponseEntity<ApiResponse> deleteSavedJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long savedJobId
    ) throws Exception {
        savedJobService.unSaveJob(candidateId, savedJobId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Job Removed from Saved Jobs", true));
    }
}
