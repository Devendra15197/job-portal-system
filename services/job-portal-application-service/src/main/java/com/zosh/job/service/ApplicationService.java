package com.zosh.job.service;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.modal.Application;
import com.zosh.job.payload.CompanyApplicationFilterRequest;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.payload.WithdrawApplicationRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) throws Exception;

    ApplicationResponse getApplicationById(Long applicationId) throws Exception;

    List<ApplicationResponse> getApplications(Long candidateId);

    List<ApplicationResponse> getApplicationsForJob(Long jobId);

    List<ApplicationResponse> getApplicationsForCompany(Long userId, CompanyApplicationFilterRequest request);

    ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) throws Exception;

    ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request) throws Exception;

    ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception;

    void deleteApplication(Long applicationId, Long candidateId) throws Exception;

    Application getApplicationEntity(Long applicationId) throws Exception;
}
