package com.zosh.job.service.impl;

import com.zosh.job.dto.WorkExperienceResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.WorkExperienceMapper;
import com.zosh.job.modal.WorkExperience;
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
    public WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId, AddWorkExperience request) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        WorkExperience workExperience = WorkExperience.builder()
                .resume(resume)
                .companyName(request.getCompanyName())
                .companyLogoUrl(request.getCompanyLogoUrl())
                .jobTitle(request.getJobTitle())
                .employmentType(request.getEmploymentType())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isCurrentJob(Boolean.TRUE.equals(request.getIsCurrentJob()))
                .description(request.getDescription())
                .technologies(request.getTechnologies())
                .displayOrder(request.getDisplayOrder())
                .build();

        WorkExperience saved = repository.save(workExperience);

        return WorkExperienceMapper.toWorkExperienceResponse(saved);

    }


    @Override
    public List<WorkExperienceResponse> getAllWorkExperiences(Long resumeId) {
        return repository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream().map(WorkExperienceMapper::toWorkExperienceResponse)
                .toList();
    }

    @Override
    public WorkExperienceResponse updateWorkExperience(Long resumeId, Long candidateId, Long workExperienceId, AddWorkExperience request) throws Exception {
        WorkExperience exp = getWorkExperienceEntity(workExperienceId);
        assertOwner(exp.getResume(), candidateId);

        exp.setCompanyName(request.getCompanyName());
        exp.setCompanyLogoUrl(request.getCompanyLogoUrl());
        exp.setJobTitle(request.getJobTitle());
        exp.setEmploymentType(request.getEmploymentType());
        exp.setLocation(request.getLocation());
        exp.setStartDate(request.getStartDate());
        exp.setEndDate(request.getEndDate());
        exp.setIsCurrentJob(Boolean.TRUE.equals(request.getIsCurrentJob()));
        exp.setDescription(request.getDescription());
        if (request.getTechnologies() != null) exp.setTechnologies(request.getTechnologies());
        if (request.getDisplayOrder() != null) exp.setDisplayOrder(request.getDisplayOrder());

        WorkExperience saved = repository.save(exp);

        return WorkExperienceMapper.toWorkExperienceResponse(saved);

    }

    @Override
    public void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId) throws Exception {
        WorkExperience exp = getWorkExperienceEntity(workExperienceId);
        assertOwner(exp.getResume(), candidateId);
        repository.delete(exp);
    }

    @Override
    public WorkExperience getWorkExperienceEntity(Long workExperienceId) throws Exception {
        return repository.findById(workExperienceId).orElseThrow(() -> new Exception("Work Experience Not Found"));
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId))
            throw new Exception("resume Not found");
    }

}
