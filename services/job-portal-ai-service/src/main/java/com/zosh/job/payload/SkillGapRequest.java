package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SkillGapRequest {
    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    private List<String> candidateSkills;
    private List<String> requiredSkills;

}
