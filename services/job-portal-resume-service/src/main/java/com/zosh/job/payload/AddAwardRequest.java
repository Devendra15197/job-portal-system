package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAwardRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private LocalDate awardDate;

    private String description;

    private String issueBy;

    private Integer displayOrder;
}

