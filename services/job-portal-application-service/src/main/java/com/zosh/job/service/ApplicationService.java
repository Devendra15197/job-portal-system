package com.zosh.job.service;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.modal.Application;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.payload.WithdrawApplicationRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request);

    ApplicationResponse getApplicationById(Long applicationId);

    List<ApplicationResponse> getApplications(Long candidateId);

    List<ApplicationResponse> getApplicationsForJob(Long jobId);

    List<ApplicationResponse> getApplicationsForCompany(Long companyId);

    ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status);

    ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request);

    ApplicationResponse toggleStar(Long applicationId, Long employerId);

    void deleteApplication(Long applicationId, Long candidateId);

    Application getApplicationEntity(Long applicationId);
}
