package com.zosh.job.service.impl;

import com.zosh.job.dto.ResumeSkillResponse;
import com.zosh.job.payload.AddResumeSkillRequest;
import com.zosh.job.service.ResumeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {
    @Override
    public ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest request) {
        return null;
    }

    @Override
    public List<ResumeSkillResponse> getAllSkills(Long resumeId, Long candidateId) {
        return List.of();
    }

    @Override
    public ResumeSkillResponse updateSkill(Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest request) {
        return null;
    }

    @Override
    public void deleteSkill(Long skillId, Long resumeId, Long candidateId) {

    }
}
