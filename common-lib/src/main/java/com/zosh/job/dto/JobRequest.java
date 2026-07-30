package com.zosh.job.dto;

import com.zosh.job.domain.ExperienceLevel;
import com.zosh.job.domain.JobType;
import com.zosh.job.domain.WorkMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Job Title is required")
    private String title;

    @NotBlank(message = "Job Description is required")
    private String description;

    private String requirements;
    private String responsibilities;
    private String benefits;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private Set<Long> skillIds;

    private Set<Long> tagIds;

    //Location - Flattened for Simple API surface
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    //Salary Flattened
    @DecimalMin(value = "0.00", message = "Minimum salary must be non-negative")
    private BigDecimal minSalary;

    @DecimalMin(value = "0.00", message = "Maximum salary must be non-negative")
    private BigDecimal maxSalary;

    //Classification
    @NotNull(message = "Job Type is required")
    private JobType jobType;

    @NotNull(message = "Work Mode is required")
    private WorkMode workMode;

    @NotNull(message = "Experience Level is required")
    private ExperienceLevel experienceLevel;

    @Min(value = 1, message = "Openings must be at least 1")
    private Integer openings = 1;

    private LocalDate applicationDeadline;
    private LocalDate expiresAt;
}
