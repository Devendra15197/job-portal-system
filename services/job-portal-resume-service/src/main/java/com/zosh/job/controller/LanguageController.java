package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.LanguageReponse;
import com.zosh.job.payload.AddLanguageRequest;
import com.zosh.job.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/languages")
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageReponse> addLanguage(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest languageRequest) throws Exception {
        return ResponseEntity.ok(languageService.addLanguage(resumeId, candidateId, languageRequest));
    }

    @GetMapping
    public ResponseEntity<List<LanguageReponse>> getLanguages(
            @PathVariable Long resumeId) throws Exception {
        return ResponseEntity.ok(languageService.getLanguages(resumeId));
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageReponse> updateLanguage(
            @PathVariable Long languageId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest languageRequest) throws Exception {
        return ResponseEntity.ok(languageService.updateLanguage(languageId, resumeId, candidateId, languageRequest));
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<ApiResponse> deleteLanguage(
            @PathVariable Long languageId,
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId) throws Exception {
        languageService.deleteLanguage(languageId, resumeId, candidateId);
        return ResponseEntity.ok(new ApiResponse("Language deleted successfully", true));
    }
}
