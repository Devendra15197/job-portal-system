package com.zosh.job.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobResponse {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private LocalDateTime savedAt;
}
