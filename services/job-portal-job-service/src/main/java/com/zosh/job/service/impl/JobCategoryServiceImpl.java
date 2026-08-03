package com.zosh.job.service.impl;

import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.mapper.JobCategoryMapper;
import com.zosh.job.modal.JobCategory;
import com.zosh.job.payload.JobCategoryRequest;
import com.zosh.job.repository.JobCategoryRepository;
import com.zosh.job.service.JobCategoryService;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCategoryRepository jobCategoryService;

    @Transactional
    @Override
    public JobCategoryResponse createJobCategory(JobCategoryRequest jobCategoryRequest) throws Exception {
        if (jobCategoryService.existsByName(jobCategoryRequest.getName())) {
            throw new Exception("Job category with name " + jobCategoryRequest.getName() + " already exists.");
        }
        JobCategory parent = null;
        if (jobCategoryRequest.getParentId() != null) {
            parent = getJobCategoryEntityById(jobCategoryRequest.getParentId());
        }
        String slug = generateUniqueSlug(jobCategoryRequest.getName());

        JobCategory jobCategory = JobCategory.builder()
                .name(jobCategoryRequest.getName())
                .slug(slug)
                .description(jobCategoryRequest.getDescription())
                .iconUrl(jobCategoryRequest.getIconUrl())
                .parent(parent)
                .active(true)
                .build();
        JobCategory savedCategory = jobCategoryService.save(jobCategory);

        return JobCategoryMapper.toJobCategoryResponse(savedCategory, true);
    }

    private String generateUniqueSlug(@NotBlank(message = "Company name is required") String name) throws Exception {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "").trim().replaceAll("[\\s-]+", "-");
        if (!jobCategoryService.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (jobCategoryService.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public List<JobCategoryResponse> getAllJobCategories() {

        List<JobCategory> jobCategories = jobCategoryService.findByActiveTrue();
        return jobCategories.stream()
                .map(category -> JobCategoryMapper.toJobCategoryResponse(category, false))
                .toList();
    }

    @Override
    public JobCategoryResponse getJobCategoryById(Long id) throws Exception {
        JobCategory jobCategory = getJobCategoryEntityById(id);
        return JobCategoryMapper.toJobCategoryResponse(jobCategory, true);
    }

    @Override
    public JobCategoryResponse updateJobCategory(Long id, JobCategoryRequest jobCategoryRequest) throws Exception {
        JobCategory jobCategory = getJobCategoryEntityById(id);

        if (!jobCategory.getName().equals(jobCategoryRequest.getName()) && jobCategoryService.existsByName(jobCategoryRequest.getName())) {
            throw new Exception("Job category with name " + jobCategoryRequest.getName() + " already exists.");
        }

        JobCategory parent = null;
        if (jobCategoryRequest.getParentId() != null) {
            if (jobCategoryRequest.getParentId().equals(id)) {
                throw new Exception("A job category cannot be its own parent.");
            }
            parent = getJobCategoryEntityById(jobCategoryRequest.getParentId());
        }

        jobCategory.setName(jobCategoryRequest.getName());
        jobCategory.setDescription(jobCategoryRequest.getDescription());
        jobCategory.setIconUrl(jobCategoryRequest.getIconUrl());
        jobCategory.setParent(parent);

        JobCategory updatedCategory = jobCategoryService.save(jobCategory);
        return JobCategoryMapper.toJobCategoryResponse(updatedCategory, true);
    }

    @Override
    public void deleteJobCategory(Long id) throws Exception {
        JobCategory jobCategory = getJobCategoryEntityById(id);
        jobCategory.setActive(false); // Mark the category as inactive instead of deleting it
        jobCategoryService.save(jobCategory);
    }

    @Override
    public JobCategory getJobCategoryEntityById(Long id) throws Exception {
        return jobCategoryService.findById(id)
                .orElseThrow(() -> new Exception("Job category not found with id: " + id));
    }
}
