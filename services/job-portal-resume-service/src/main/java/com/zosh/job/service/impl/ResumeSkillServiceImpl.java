package com.zosh.job.service.impl;

import com.zosh.job.dto.ResumeSkillResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.entity.ResumeSkill;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.payload.AddResumeSkillRequest;
import com.zosh.job.repository.ResumeSkillRepository;
import com.zosh.job.service.ResumeService;
import com.zosh.job.service.ResumeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {

    private final ResumeSkillRepository repository;
    private final ResumeService resumeService;

    @Override
    public ResumeSkillResponse addSkill(Long resumeId, Long candidateId, AddResumeSkillRequest request) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        ResumeSkill skill = ResumeSkill.builder()
                .resume(resume)
                .skillName(request.getSkillName())
                .proficiencyLevel(request.getProficiencyLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        ResumeSkill saved = repository.save(skill);
        return ResumeMapper.toResumeSkillResponse(saved);
    }

    @Override
    public List<ResumeSkillResponse> getAllSkills(Long resumeId) {
        return repository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toResumeSkillResponse).toList();
    }

    @Override
    public ResumeSkillResponse updateSkill(Long skillId, Long resumeId, Long candidateId, AddResumeSkillRequest request) throws Exception {
        ResumeSkill skill = repository.findById(skillId).orElseThrow(() -> new Exception("Skill not found"));
        assertOwner(skill.getResume(), candidateId);

        skill.setSkillName(request.getSkillName());
        skill.setProficiencyLevel(request.getProficiencyLevel());
        skill.setYearsOfExperience(request.getYearsOfExperience());
        skill.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);

        ResumeSkill saved = repository.save(skill);
        return ResumeMapper.toResumeSkillResponse(saved);
    }

    @Override
    public void deleteSkill(Long skillId, Long resumeId, Long candidateId) throws Exception {
        ResumeSkill skill = repository.findById(skillId).orElseThrow(() -> new Exception("Skill not found"));
        assertOwner(skill.getResume(), candidateId);

        repository.delete(skill);
    }


    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId))
            throw new Exception("resume Not found");
    }
}
