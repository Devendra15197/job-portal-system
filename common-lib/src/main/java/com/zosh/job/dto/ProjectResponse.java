package com.zosh.job.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long id;

    private String title;
    private String description;
    private List<String> technologies;

    private String projectUrl;

    private String sourceCodeUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isOngoing;

    private Integer displayOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
