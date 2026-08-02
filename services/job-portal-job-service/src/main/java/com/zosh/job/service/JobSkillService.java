package com.zosh.job.service;

import com.zosh.job.dto.JobSkillResponse;
import com.zosh.job.modal.JobSkill;
import com.zosh.job.payload.JobSkillRequest;

import java.util.List;
import java.util.Set;

public interface JobSkillService {
    JobSkillResponse createJobSkill(JobSkillRequest jobSkillRequest) throws Exception;

    List<JobSkillResponse> getAllJobSkills();

    JobSkillResponse getJobSkillById(Long id);

    JobSkillResponse updateJobSkill(Long id, JobSkillRequest jobSkillRequest);

    void deleteJobSkill(Long id);

    Set<JobSkill> getSkillByIds(Set<Long> ids);

}
