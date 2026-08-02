package com.zosh.job.mapper;

import com.zosh.job.dto.JobSkillResponse;
import com.zosh.job.modal.JobSkill;

public class JobSkillMapper {

    public static JobSkillResponse toJobSkillResponse(JobSkill jobSkill) {
        if (jobSkill == null) {
            return null;
        }

        return JobSkillResponse.builder()
                .id(jobSkill.getId())
                .name(jobSkill.getName())
                .slug(jobSkill.getSlug())
                .category(jobSkill.getCategory().name())
                .active(jobSkill.getActive())
                .build();
    }
}
