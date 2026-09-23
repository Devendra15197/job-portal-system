package com.zosh.job.controller;

import com.zosh.job.payload.*;
import com.zosh.job.service.ResumeAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/resume")
public class ResumeAiController {
    private final ResumeAiService resumeAiService;

    @PostMapping("/summary")
    public ResponseEntity<AiTextResponse> generateSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest) throws Exception {
        AiTextResponse response = resumeAiService.generateProfessionalSummary(resumeSummaryRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/experience-bullets")
    public ResponseEntity<WorkExperienceBulletsResponse> generateBullets(@RequestBody WorkExperienceBulletsRequest workExperienceBulletsRequest) throws Exception {
        WorkExperienceBulletsResponse response = resumeAiService.generateWorkExperienceBullets(workExperienceBulletsRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/improvements")
    public ResponseEntity<ResumeImprovementResponse> getImprovements(@RequestBody ResumeImprovementRequest resumeImprovementRequest) throws Exception {
        ResumeImprovementResponse response = resumeAiService.getResumeImprovementTips(resumeImprovementRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/career-feedback")
    public ResponseEntity<CareerFeedbackResponse> generate(@RequestBody CareerFeedbackRequest careerFeedbackRequest) throws Exception {
        CareerFeedbackResponse response = resumeAiService.getCareerFeedback(careerFeedbackRequest);
        return ResponseEntity.ok(response);
    }

}
