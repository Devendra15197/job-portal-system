package com.zosh.job.dto;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private UserResponse candidate;
    private Long employerId;

    private JobResponse job;
    private CompanyResponse company;

    private ApplicationStatus status;

    private Long resumeId;
    private String coverLetter;

    private BigDecimal expectedSalary;
    private LocalDate availableFrom;

    private Boolean isStarred;

    //TODO
    //private List<ApplicationStatusHistoryResponse> statusHistory;
    // private List<InterviewResponse> interviews;
    private List<ApplicationNoteResponse> notes;

    private LocalDateTime withdrawnAt;
    private String withdrawnReason;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    //private ApplicationScreeningResponse screening;
}
