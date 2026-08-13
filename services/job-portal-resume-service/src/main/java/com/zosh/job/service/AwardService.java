package com.zosh.job.service;

import com.zosh.job.dto.AwardResponse;
import com.zosh.job.payload.AddAwardRequest;

import java.util.List;

public interface AwardService {
    AwardResponse addAward(Long resumeId, Long candidateId, AddAwardRequest awardRequest) throws Exception;

    List<AwardResponse> getAwards(Long resumeId);

    AwardResponse updateAward(Long awardId, Long resumeId, Long candidateId, AddAwardRequest awardRequest) throws Exception;

    void deleteAward(Long awardId, Long resumeId, Long candidateId) throws Exception;
}

