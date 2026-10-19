package com.zosh.job.event;

import com.zosh.job.domain.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusChangedEvent {

    private Long applicationId;
    private Long candidateId;
    private String candidateEmail;
    private String candidateName;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private String note;
    private String jobTitle;
    private String companyName;
    private LocalDateTime changedAt;
}
