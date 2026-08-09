package com.zosh.job.payload;

import com.zosh.job.domain.ProficiencyLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddResumeSkillRequest {
    @NotBlank(message = "Skill name cannot be blank")
    @Size(max = 100, message = "Skill name cannot exceed 100 characters")
    private String skillName;

    @NotNull(message = "Proficiency level cannot be null")
    private ProficiencyLevel proficiencyLevel;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private Integer displayOrder;
}
