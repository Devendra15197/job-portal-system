package com.zosh.job.service;

import com.zosh.job.dto.PersonalInfoResponse;
import com.zosh.job.dto.ResumeResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.PersonalInfo;
import com.zosh.job.payload.CreateResumeRequest;
import com.zosh.job.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    @Override
    public ResumeResponse createResume(Long candidateId, CreateResumeRequest resumeRequest) {
        if (Boolean.TRUE.equals(resumeRequest.getIsDefault())) {
            resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        resumeRepository.save(existing);
                    });
        }

        Resume resume = Resume.builder()
                .candidateId(candidateId)
                .title(resumeRequest.getTitle())
                .template(resumeRequest.getTemplate())
                .visibility(resumeRequest.getVisibility())
                .isDefault(Boolean.TRUE.equals(resumeRequest.getIsDefault()))
                .isActive(true)
                .build();
        Resume saved = resumeRepository.save(resume);
        return buildFullResponse(saved);
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        return buildFullResponse(resume);
    }

    @Override
    public List<ResumeResponse> getMyResumes(Long candidateId) {
        return resumeRepository.findByCandidateIdAndIsActiveTrue(candidateId).stream()
                .map(this::buildFullResponse)
                .toList();
    }

    @Override
    public ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfoResponse req) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        PersonalInfo info = resume.getPersonalInfo();
        if (info == null) {
            info = new PersonalInfo();
        }
        if (req.getFirstName() != null) {
            info.setFirstName(req.getFirstName());
        }
        if (req.getLastName() != null) {
            info.setLastName(req.getLastName());
        }
        if (req.getHeadline() != null) info.setHeadline(req.getHeadline());
        if (req.getEmail() != null) info.setEmail(req.getEmail());
        if (req.getPhone() != null) info.setPhone(req.getPhone());
        if (req.getCity() != null) info.setCity(req.getCity());
        if (req.getCountry() != null) info.setCountry(req.getCountry());
        if (req.getLinkedinUrl() != null) info.setLinkedinUrl(req.getLinkedinUrl());
        if (req.getGithubUrl() != null) info.setGithubUrl(req.getGithubUrl());
        if (req.getPortfolioUrl() != null) info.setPortfolioUrl(req.getPortfolioUrl());
        if (req.getWebsiteUrl() != null) info.setWebsiteUrl(req.getWebsiteUrl());

        resume.setPersonalInfo(info);
        Resume updated = resumeRepository.save(resume);
        return buildFullResponse(updated);
    }

    @Override
    public ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        resume.setSummary(summary);
        Resume updated = resumeRepository.save(resume);
        return buildFullResponse(updated);
    }

    @Override
    public ResumeResponse setDefaultResume(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        resumeRepository.findByCandidateIdAndIsDefaultTrue(candidateId)
                .ifPresent(existing -> {
                    existing.setIsDefault(false);
                    resumeRepository.save(existing);
                });
        resume.setIsDefault(true);
        Resume updated = resumeRepository.save(resume);
        return buildFullResponse(updated);
    }

    @Override
    public void deleteResume(Long resumeId, Long candidateId) throws Exception {
        Resume resume = getResumeEntity(resumeId);
        assertOwner(resume, candidateId);
        resume.setIsActive(false);
        resume.setIsDefault(false);
        resumeRepository.save(resume);
    }

    @Override
    public Resume getResumeEntity(Long resumeId) throws Exception {
        return resumeRepository.findById(resumeId).orElseThrow(() -> new Exception("Resume not found with Id " + resumeId));
    }

    private ResumeResponse buildFullResponse(Resume resume) {
        return ResumeMapper.toResumeResponse(resume);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}
