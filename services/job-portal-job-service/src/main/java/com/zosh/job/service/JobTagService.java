package com.zosh.job.service;

import com.zosh.job.dto.JobTagResponse;
import com.zosh.job.modal.JobTag;
import com.zosh.job.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {
    JobTagResponse createJobTag(JobTagRequest jobTagRequest);

    List<JobTagResponse> getAllJobTags();

    JobTagResponse getJobTagById(Long id);

    JobTagResponse updateJobTag(Long id, JobTagRequest jobTagRequest);

    void deleteJobTag(Long id);

    JobTag getJobTagByIdEntity(Long id);

    Set<JobTag> getJobTagsByIds(Set<Long> ids);
}
