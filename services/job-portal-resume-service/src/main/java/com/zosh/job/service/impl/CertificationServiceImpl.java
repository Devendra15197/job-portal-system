package com.zosh.job.service.impl;

import com.zosh.job.dto.CertificationResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.dto.CertificationResponse;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.Certification;
import com.zosh.job.payload.AddCertificationRequest;
import com.zosh.job.repository.CertificationRepository;
import com.zosh.job.service.CertificationService;
import com.zosh.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {
    private final CertificationRepository certificationRepository;
    private final ResumeService resumeService;

    @Override
    public CertificationResponse addCertification(Long resumeId, Long candidateId, AddCertificationRequest certificationRequest) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Certification certification = Certification.builder()
                .resume(resume)
                .certificationName(certificationRequest.getCertificationName())
                .issuingOrganization(certificationRequest.getIssuingOrganization())
                .issueDate(certificationRequest.getIssueDate())
                .expirationDate(certificationRequest.getExpirationDate())
                .displayOrder(certificationRequest.getDisplayOrder())
                .build();

        Certification saved = certificationRepository.save(certification);
        return ResumeMapper.toCertificationResponse(saved);
    }

    @Override
    public List<CertificationResponse> getCertifications(Long resumeId) {
        return certificationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toCertificationResponse).toList();
    }

    @Override
    public CertificationResponse updateCertification(Long certificationId, Long resumeId, Long candidateId, AddCertificationRequest certificationRequest) throws Exception {
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new Exception("Certification not found with Id " + certificationId));

        assertOwner(certification.getResume(), candidateId);

        certification.setCertificationName(certificationRequest.getCertificationName());
        certification.setIssuingOrganization(certificationRequest.getIssuingOrganization());
        certification.setIssueDate(certificationRequest.getIssueDate());
        certification.setExpirationDate(certificationRequest.getExpirationDate());
        if (certificationRequest.getDisplayOrder() != null)
            certification.setDisplayOrder(certificationRequest.getDisplayOrder());

        Certification updated = certificationRepository.save(certification);
        return ResumeMapper.toCertificationResponse(updated);
    }

    @Override
    public void deleteCertification(Long certificationId, Long resumeId, Long candidateId) throws Exception {
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new Exception("Certification not found with Id " + certificationId));

        assertOwner(certification.getResume(), candidateId);

        certificationRepository.delete(certification);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}

