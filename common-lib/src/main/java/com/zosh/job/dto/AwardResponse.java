package com.zosh.job.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardResponse {
    private Long id;
    private String title;
    private LocalDate awardDate;
    private String description;
    private String issueBy;
    private Integer displayOrder;
}

