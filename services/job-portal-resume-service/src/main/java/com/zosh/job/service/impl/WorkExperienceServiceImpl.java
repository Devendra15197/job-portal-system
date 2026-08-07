package com.zosh.job.service.impl;

import com.zosh.job.dto.WorkExperienceResponse;
import com.zosh.job.payload.AddWorkExperience;
import com.zosh.job.repository.WorkExperienceRepository;
import com.zosh.job.service.ResumeService;
import com.zosh.job.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceRepository repository;
    private final ResumeService resumeService;

    @Override
    public WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId, AddWorkExperience request) {
        return null;
    }

    @Override
    public List<WorkExperienceResponse> getAllWorkExperiences(Long resumeId) {
        return List.of();
    }

    @Override
    public WorkExperienceResponse updateWorkExperience(Long resumeId, Long workExperienceId, AddWorkExperience request) {
        return null;
    }

    @Override
    public void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId) {

    }
}
