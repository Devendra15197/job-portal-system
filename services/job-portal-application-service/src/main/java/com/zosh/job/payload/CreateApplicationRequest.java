package com.zosh.job.payload;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateApplicationRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Resume ID is required")
    private Long resumeId;

    @Size(max = 5000, message = "Cover letter should not exceed 5000 characters")
    private String coverLetter;

    @DecimalMin(value = "0.00", message = "Expected salary should be a non-negative value")
    private BigDecimal expectedSalary;

    private LocalDate availableFrom;

}
