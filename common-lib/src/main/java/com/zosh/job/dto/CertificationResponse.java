package com.zosh.job.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationResponse {
    private Long id;
    private String certificationName;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private Integer displayOrder;
}

