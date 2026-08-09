package com.zosh.job.service;

import com.zosh.job.dto.ResumeSkillResponse;
import com.zosh.job.payload.AddResumeSkillRequest;

import java.util.List;

public interface ResumeSkillService {

    ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest request) throws Exception;

    List<ResumeSkillResponse> getAllSkills(Long resumeId, Long candidateId);

    ResumeSkillResponse updateSkill(Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest request) throws Exception;

    void deleteSkill(Long skillId, Long resumeId, Long candidateId) throws Exception;

}
