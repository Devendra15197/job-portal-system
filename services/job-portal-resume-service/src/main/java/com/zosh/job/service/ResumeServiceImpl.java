package com.zosh.job.service;

import com.zosh.job.dto.PersonalInfo;
import com.zosh.job.dto.ResumeResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.payload.CreateResumeRequest;
import com.zosh.job.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository repository;

    @Override
    public ResumeResponse createResume(Long candidateId, CreateResumeRequest resumeRequest) {
        if (Boolean.TRUE.equals(resumeRequest.getIsDefault())) {
            List<Resume> resumes = repository.findByCandidateId(candidateId);

        }
        return null;
    }

    @Override
    public ResumeResponse getResumeById(Long resumeId, Long candidateId) {
        return null;
    }

    @Override
    public List<ResumeResponse> getMyResumes(Long candidateId) {
        return List.of();
    }

    @Override
    public ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfo req) {
        return null;
    }

    @Override
    public ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) {
        return null;
    }

    @Override
    public ResumeResponse setDefaultResume(Long resumeId, Long candidateId) {
        return null;
    }

    @Override
    public void deleteResume(Long resumeId, Long candidateId) {

    }

    @Override
    public Resume getResumeEntity(Long resumeId) {
        return null;
    }
}
