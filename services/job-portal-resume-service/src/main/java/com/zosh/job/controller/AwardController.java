package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.AwardResponse;
import com.zosh.job.payload.AddAwardRequest;
import com.zosh.job.service.AwardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/awards")
public class AwardController {

    private final AwardService awardService;

    @PostMapping
    public ResponseEntity<AwardResponse> createAward(
            @PathVariable("resumeId") Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest awardRequest) throws Exception {
        return ResponseEntity.ok(awardService.addAward(resumeId, candidateId, awardRequest));
    }

    @GetMapping
    public ResponseEntity<List<AwardResponse>> getAwards(@PathVariable("resumeId") Long resumeId) {
        return ResponseEntity.ok(awardService.getAwards(resumeId));
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<AwardResponse> updateAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddAwardRequest awardRequest) throws Exception {
        return ResponseEntity.ok(awardService.updateAward(awardId, resumeId, candidateId, awardRequest));
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<ApiResponse> deleteAward(
            @PathVariable Long resumeId,
            @PathVariable Long awardId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        awardService.deleteAward(awardId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Award Deleted Successfully", true));
    }
}

