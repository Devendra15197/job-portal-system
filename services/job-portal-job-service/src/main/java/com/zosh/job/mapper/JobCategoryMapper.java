package com.zosh.job.mapper;


import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.modal.JobCategory;

import java.util.List;

public class JobCategoryMapper {

    public static JobCategoryResponse toJobCategoryResponse(JobCategory jobCategory, boolean includeChildren) {
        if (jobCategory == null) {
            return null;
        }

        List<JobCategoryResponse> subCategories = null;

        if (includeChildren) {
            subCategories = jobCategory.getSubCategories()
                    .stream()
                    .map(subCategory -> toJobCategoryResponse(subCategory, false))
                    .toList();
        }
        return JobCategoryResponse.builder()
                .id(jobCategory.getId())
                .name(jobCategory.getName())
                .slug(jobCategory.getSlug())
                .description(jobCategory.getDescription())
                .iconUrl(jobCategory.getIconUrl())
                .parentId(jobCategory.getParent() != null ? jobCategory.getParent().getId() : null)
                .parentName(jobCategory.getParent() != null ? jobCategory.getParent().getName() : null)
                .subCategories(subCategories)
                .createdAt(jobCategory.getCreatedAt())
                .build();
    }
}
