package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.JobSkillResponse;
import com.zosh.job.payload.JobSkillRequest;
import com.zosh.job.service.JobSkillService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-skills")
public class JobSkillController {

    private final JobSkillService jobSkillService;

    @PostMapping
    public ResponseEntity<JobSkillResponse> createJobSkill(@RequestBody @Valid JobSkillRequest jobSkillRequest) {
        try {
            JobSkillResponse jobSkillResponse = jobSkillService.createJobSkill(jobSkillRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jobSkillResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobSkillResponse> getSkillsById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(jobSkillService.getJobSkillById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<JobSkillResponse>> getAllJobSkills() {
        try {
            List<JobSkillResponse> jobSkills = jobSkillService.getAllJobSkills();
            return ResponseEntity.ok(jobSkills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSkillResponse> updateJobSkill(@PathVariable Long id, @RequestBody @Valid JobSkillRequest jobSkillRequest) {
        try {
            JobSkillResponse updatedJobSkill = jobSkillService.updateJobSkill(id, jobSkillRequest);
            return ResponseEntity.ok(updatedJobSkill);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJobSkill(@PathVariable Long id) {
        try {
            jobSkillService.deleteJobSkill(id);
            return ResponseEntity.ok(new ApiResponse("Job skill deleted successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to delete job skill", false));
        }
    }

}
