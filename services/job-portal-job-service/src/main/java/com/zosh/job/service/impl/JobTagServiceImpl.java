package com.zosh.job.service.impl;

import com.zosh.job.dto.JobTagResponse;
import com.zosh.job.mapper.JobTagMapper;
import com.zosh.job.modal.JobTag;
import com.zosh.job.payload.JobTagRequest;
import com.zosh.job.repository.JobTagRepository;
import com.zosh.job.service.JobTagService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {

    private final JobTagRepository jobTagRepository;

    @Override
    public JobTagResponse createJobTag(JobTagRequest jobTagRequest) {
        if (jobTagRepository.existsByName(jobTagRequest.getName())) {
            throw new RuntimeException("Job tag with the same name already exists");
        }

        String slug = generateUniqueSlug(jobTagRequest.getName());


        JobTag jobTag = JobTag.builder()
                .name(jobTagRequest.getName())
                .slug(slug)
                .build();

        JobTag savedJobTag = jobTagRepository.save(jobTag);

        return JobTagMapper.toJobTagResponse(savedJobTag);
    }

    @Override
    public List<JobTagResponse> getAllJobTags() {
        List<JobTag> jobTags = jobTagRepository.findAll();
        return jobTags.stream()
                .map(JobTagMapper::toJobTagResponse)
                .toList();
    }

    @Override
    public JobTagResponse getJobTagById(Long id) {
        JobTag jobTag = getJobTagByIdEntity(id); // Ensure the job tag exists
        return JobTagMapper.toJobTagResponse(jobTag);
    }

    @Override
    public JobTagResponse updateJobTag(Long id, JobTagRequest jobTagRequest) {
        JobTag existingJobTag = getJobTagByIdEntity(id);
        if (existingJobTag == null) {
            throw new RuntimeException("Job tag not found with id: " + id);
        }

        if (!existingJobTag.getName().equals(jobTagRequest.getName()) && jobTagRepository.existsByName(jobTagRequest.getName())) {
            throw new RuntimeException("Job tag with the same name already exists");
        }

        existingJobTag.setName(jobTagRequest.getName());

        JobTag updatedJobTag = jobTagRepository.save(existingJobTag);

        return JobTagMapper.toJobTagResponse(updatedJobTag);
    }

    @Override
    public void deleteJobTag(Long id) {
        JobTag existingJobTag = getJobTagByIdEntity(id);
        if (existingJobTag == null) {
            throw new RuntimeException("Job tag not found with id: " + id);
        }
        jobTagRepository.delete(existingJobTag);
    }

    @Override
    public JobTag getJobTagByIdEntity(Long id) {
        return jobTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job tag not found with id: " + id));
    }

    @Override
    public Set<JobTag> getJobTagsByIds(Set<Long> ids) {
        return new HashSet<>(jobTagRepository.findAllById(ids));
    }

    private String generateUniqueSlug(@NotBlank(message = "Company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "").trim().replaceAll("[\\s-]+", "-");
        if (!jobTagRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobTagRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }
}
