package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CoverLetterRequest {
    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    private String JobDescription;

    @NotBlank(message = "Candidate name cannot be blank")
    private String candidateName;

    private String candidateSummary;

    private List<String> candidateSkills;
    private List<String> candidateExperience;
    private String targetCompanyName;
}
