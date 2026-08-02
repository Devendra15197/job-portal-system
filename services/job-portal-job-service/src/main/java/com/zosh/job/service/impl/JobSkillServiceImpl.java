package com.zosh.job.service.impl;

import com.zosh.job.dto.JobSkillResponse;
import com.zosh.job.mapper.JobSkillMapper;
import com.zosh.job.modal.JobSkill;
import com.zosh.job.payload.JobSkillRequest;
import com.zosh.job.repository.JobSkillRepository;
import com.zosh.job.service.JobSkillService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobSkillServiceImpl implements JobSkillService {

    private final JobSkillRepository jobSkillRepository;

    @Override
    public JobSkillResponse createJobSkill(JobSkillRequest jobSkillRequest) throws Exception {
        if (jobSkillRepository.existsByName(jobSkillRequest.getName())) {
            throw new Exception("Job skill with the same name already exists");
        }
        String slug = generateUniqueSlug(jobSkillRequest.getName());
        // Continue with the rest of the logic
        JobSkill jobSkill = JobSkill.builder()
                .name(jobSkillRequest.getName())
                .slug(slug)
                //.active(jobSkillRequest.isActive())
                .category(jobSkillRequest.getCategory())
                .build();

        return JobSkillMapper.toJobSkillResponse(jobSkillRepository.save(jobSkill));
    }


    @Override
    public List<JobSkillResponse> getAllJobSkills() {
        List<JobSkill> jobSkills = jobSkillRepository.findByActiveTrue();
        return jobSkills.stream()
                .map(JobSkillMapper::toJobSkillResponse)
                .toList();
    }

    @Override
    public JobSkillResponse getJobSkillById(Long id) {
        JobSkill jobSkill = jobSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job skill not found with id: " + id));
        return JobSkillMapper.toJobSkillResponse(jobSkill);
    }

    @Override
    public JobSkillResponse updateJobSkill(Long id, JobSkillRequest jobSkillRequest) {
        JobSkill existingJobSkill = jobSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job skill not found with id: " + id));

        if (!existingJobSkill.getName().equals(jobSkillRequest.getName()) && jobSkillRepository.existsByName(jobSkillRequest.getName())) {
            throw new RuntimeException("Job skill with the same name already exists");
        }

        existingJobSkill.setName(jobSkillRequest.getName());
        existingJobSkill.setCategory(jobSkillRequest.getCategory());
        // Update other fields as necessary

        return JobSkillMapper.toJobSkillResponse(jobSkillRepository.save(existingJobSkill));
    }

    @Override
    public void deleteJobSkill(Long id) {
        if (!jobSkillRepository.existsById(id)) {
            throw new RuntimeException("Job skill not found with id: " + id);
        }
        jobSkillRepository.deleteById(id);
    }


    @Override
    public Set<JobSkill> getSkillByIds(Set<Long> ids) {
        return new HashSet<>(jobSkillRepository.findAllById(ids));
    }


    private String generateUniqueSlug(@NotBlank(message = "Company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "").trim().replaceAll("[\\s-]+", "-");
        if (!jobSkillRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobSkillRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }
}
