package com.zosh.job.service;

import com.zosh.job.dto.CertificationResponse;
import com.zosh.job.payload.AddCertificationRequest;

import java.util.List;

public interface CertificationService {
    CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest certificationRequest) throws Exception;

    List<CertificationResponse> getCertifications(Long resumeId);

    CertificationResponse updateCertification(Long certificationId, Long resumeId, Long candidateId, AddCertificationRequest certificationRequest) throws Exception;

    void deleteCertification(Long certificationId, Long resumeId, Long candidateId) throws Exception;
}

