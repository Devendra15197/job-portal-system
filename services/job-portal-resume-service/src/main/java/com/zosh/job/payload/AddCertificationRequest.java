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
public class AddCertificationRequest {

    @NotBlank(message = "Certification name cannot be blank")
    private String certificationName;

    private String issuingOrganization;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private Integer displayOrder;
}

