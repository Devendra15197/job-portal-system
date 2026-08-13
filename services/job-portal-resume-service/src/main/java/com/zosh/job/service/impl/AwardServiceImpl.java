package com.zosh.job.service.impl;

import com.zosh.job.dto.AwardResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.Award;
import com.zosh.job.payload.AddAwardRequest;
import com.zosh.job.repository.AwardRepository;
import com.zosh.job.service.AwardService;
import com.zosh.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {
    private final AwardRepository awardRepository;
    private final ResumeService resumeService;

    @Override
    public AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest awardRequest) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Award award = Award.builder()
                .resume(resume)
                .title(awardRequest.getTitle())
                .awardDate(awardRequest.getAwardDate())
                .description(awardRequest.getDescription())
                .issueBy(awardRequest.getIssueBy())
                .displayOrder(awardRequest.getDisplayOrder())
                .build();

        Award saved = awardRepository.save(award);
        return ResumeMapper.toAwardResponse(saved);
    }

    @Override
    public List<AwardResponse> getAwards(Long resumeId) {
        return awardRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toAwardResponse)
                .toList();
    }

    @Override
    public AwardResponse updateAward(Long awardId, Long resumeId, Long candidateId, AddAwardRequest awardRequest) throws Exception {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new Exception("Award not found with Id " + awardId));

        assertOwner(award.getResume(), candidateId);

        award.setTitle(awardRequest.getTitle());
        award.setAwardDate(awardRequest.getAwardDate());
        award.setDescription(awardRequest.getDescription());
        award.setIssueBy(awardRequest.getIssueBy());
        if (awardRequest.getDisplayOrder() != null) award.setDisplayOrder(awardRequest.getDisplayOrder());

        Award updated = awardRepository.save(award);
        return ResumeMapper.toAwardResponse(updated);
    }

    @Override
    public void deleteAward(Long awardId, Long resumeId, Long candidateId) throws Exception {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new Exception("Award not found with Id " + awardId));

        assertOwner(award.getResume(), candidateId);

        awardRepository.delete(award);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}

