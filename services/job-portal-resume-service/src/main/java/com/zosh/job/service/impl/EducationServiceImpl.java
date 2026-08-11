package com.zosh.job.service.impl;

import com.zosh.job.dto.EducationResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.Education;
import com.zosh.job.payload.AddEducationRequest;
import com.zosh.job.repository.EducationRepository;
import com.zosh.job.service.EducationService;
import com.zosh.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;
    private final ResumeService resumeService;

    @Override
    public EducationResponse addEducation(Long resumeId, Long candidateId, AddEducationRequest educationRequest) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Education education = Education
                .builder()
                .resume(resume)
                .institutionName(educationRequest.getInstitutionName())
                .degree(educationRequest.getDegree())
                .fieldOfStudy(educationRequest.getFieldOfStudy())
                .grade(educationRequest.getGrade())
                .startDate(educationRequest.getStartDate())
                .endDate(educationRequest.getEndDate())
                .isCurrentlyStudying(educationRequest.getIsCurrentlyStudying())
                .description(educationRequest.getDescription())
                .displayOrder(educationRequest.getDisplayOrder())
                .build();

        Education savedEducation = educationRepository.save(education);
        return ResumeMapper.toEducationResponse(savedEducation);
    }

    @Override
    public List<EducationResponse> getEducations(Long resumeId) {
        return educationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toEducationResponse).toList();
    }

    @Override
    public EducationResponse updateEducation(Long educationId, Long resumeId, Long candidateId, AddEducationRequest educationRequest) throws Exception {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new Exception("Education not found with Id " + educationId));

        assertOwner(education.getResume(), candidateId);

        education.setInstitutionName(education.getInstitutionName());
        education.setDegree(education.getDegree());
        education.setFieldOfStudy(education.getFieldOfStudy());
        education.setGrade(education.getGrade());
        education.setStartDate(education.getStartDate());
        education.setEndDate(education.getEndDate());
        education.setIsCurrentlyStudying(Boolean.TRUE.equals(education.getIsCurrentlyStudying()));
        education.setDescription(education.getDescription());
        if (education.getDisplayOrder() != null) education.setDisplayOrder(educationRequest.getDisplayOrder());

        return ResumeMapper.toEducationResponse(education);
    }

    @Override
    public void deleteEducation(Long educationId, Long resumeId, Long candidateId) throws Exception {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new Exception("Education not found with Id " + educationId));

        assertOwner(education.getResume(), candidateId);

        educationRepository.delete(education);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}
