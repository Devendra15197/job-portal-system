package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SalaryRangeRequest {
    @NotBlank(message = "Job title cannot be blank")
    private String title;

    private List<String> skills;
    private String experienceLevel;
    private String jobType;
    private String location;
}
