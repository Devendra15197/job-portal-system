package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.CertificationResponse;
import com.zosh.job.payload.AddCertificationRequest;
import com.zosh.job.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<CertificationResponse> createCertification(
            @PathVariable("resumeId") Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest certificationRequest) throws Exception {
        return ResponseEntity.ok(certificationService.addCertification(resumeId, candidateId, certificationRequest));
    }

    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getCertifications(@PathVariable("resumeId") Long resumeId) {
        return ResponseEntity.ok(certificationService.getCertifications(resumeId));
    }

    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddCertificationRequest certificationRequest) throws Exception {
        return ResponseEntity.ok(certificationService.updateCertification(certificationId, resumeId, candidateId, certificationRequest));
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<ApiResponse> deleteCertification(
            @PathVariable Long resumeId,
            @PathVariable Long certificationId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        certificationService.deleteCertification(certificationId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Certification Deleted Successfully", true));
    }
}

