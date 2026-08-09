package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.ResumeSkillResponse;
import com.zosh.job.payload.AddResumeSkillRequest;
import com.zosh.job.service.ResumeSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/skills")
public class ResumeSkillController {
    private final ResumeSkillService resumeSkillService;

    @PostMapping
    public ResponseEntity<ResumeSkillResponse> addSkill(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddResumeSkillRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeSkillService.addSkill(resumeId, candidateId, request));
    }


    @GetMapping
    public ResponseEntity<List<ResumeSkillResponse>> getAllSkillsByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ok(resumeSkillService.getAllSkills(resumeId));
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ResumeSkillResponse> updateSkill(
            @PathVariable Long resumeId,
            @PathVariable Long skillId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddResumeSkillRequest request) throws Exception {
        return ResponseEntity.ok(resumeSkillService.updateSkill(skillId, resumeId, candidateId, request));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse> deleteSkill(
            @PathVariable Long resumeId,
            @PathVariable Long skillId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        resumeSkillService.deleteSkill(skillId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Skill Deleted Successfully", true));
    }

}
