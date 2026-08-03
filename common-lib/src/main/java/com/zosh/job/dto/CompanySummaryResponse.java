package com.zosh.job.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanySummaryResponse {

    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private String websiteUrl;
    private String description;
    private Boolean active;
}
