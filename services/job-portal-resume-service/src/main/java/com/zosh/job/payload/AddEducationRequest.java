package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEducationRequest {

    @NotBlank(message = "Institution name cannot be blank")
    private String institutionName;

    @NotBlank(message = "Degree cannot be blank")
    private String degree;

    private String fieldOfStudy;
    private String grade;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean isCurrentlyStudying = false;

    private String description;
    private Integer displayOrder;
}
