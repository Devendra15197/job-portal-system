package com.zosh.job.dto;

import com.zosh.job.domain.ProficiencyLevel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeSkillResponse {
    private Long id;

    private String skillName;

    private ProficiencyLevel proficiencyLevel;

    private Integer yearsOfExperience;

    private Integer displayOrder;
}
