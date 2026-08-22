package com.zosh.job.mapper;

import com.zosh.job.dto.SavedJobResponse;
import com.zosh.job.modal.SavedJob;

public class PreferenceMapper {

    public static SavedJobResponse toSavedJobResponse(SavedJob req) {
        return SavedJobResponse.builder()
                .id(req.getId())
                .candidateId(req.getCandidateId())
                .jobId(req.getJobId())
                .savedAt(req.getSavedAt())
                .build();
    }
}
