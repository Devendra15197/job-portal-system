package com.zosh.job.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private Boolean active;

    private Long parentId; //to find out the parent category of this category
    private String parentName; //to find out the parent category name of this category
    private List<JobCategoryResponse> subCategories; //to find out subcategories of this category

    private LocalDateTime createdAt;
}
