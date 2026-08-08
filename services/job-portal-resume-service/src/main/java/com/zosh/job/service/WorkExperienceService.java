package com.zosh.job.service;

import com.zosh.job.dto.WorkExperienceResponse;
import com.zosh.job.modal.WorkExperience;
import com.zosh.job.payload.AddWorkExperience;

import java.util.List;

public interface WorkExperienceService {
    WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId, AddWorkExperience request) throws Exception;

    List<WorkExperienceResponse> getAllWorkExperiences(Long resumeId);

    WorkExperienceResponse updateWorkExperience(Long resumeId, Long candidateId, Long workExperienceId, AddWorkExperience request) throws Exception;

    void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId) throws Exception;

    WorkExperience getWorkExperienceEntity(Long workExperienceId) throws Exception;

}
