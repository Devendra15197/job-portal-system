package com.zosh.job.service;

import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.modal.JobCategory;
import com.zosh.job.payload.JobCategoryRequest;

import java.util.List;

public interface JobCategoryService {
    JobCategoryResponse createJobCategory(JobCategoryRequest jobCategoryRequest) throws Exception;
    List<JobCategoryResponse> getAllJobCategories();
    JobCategoryResponse getJobCategoryById(Long id) throws Exception;
    JobCategoryResponse updateJobCategory(Long id, JobCategoryRequest jobCategoryRequest) throws Exception;
    void deleteJobCategory(Long id) throws Exception;

    JobCategory getJobCategoryEntityById(Long id) throws Exception;

}
