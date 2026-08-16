package com.zosh.job.service.impl;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.modal.Application;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.payload.WithdrawApplicationRequest;
import com.zosh.job.repository.ApplicationRepository;
import com.zosh.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repository;

    @Override
    public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) {
        if(repository.existsByCandidateIdAndJobId(candidateId, request.getJobId())){
            throw new Exception("You have already applied to this application");
        }
        //fetch job
        //fetch resume


    }

    @Override
    public ApplicationResponse getApplicationById(Long applicationId) {
        return null;
    }

    @Override
    public List<ApplicationResponse> getApplications(Long candidateId) {
        return List.of();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return List.of();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCompany(Long companyId) {
        return List.of();
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) {
        return null;
    }

    @Override
    public ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request) {
        return null;
    }

    @Override
    public ApplicationResponse toggleStar(Long applicationId, Long employerId) {
        return null;
    }

    @Override
    public void deleteApplication(Long applicationId, Long candidateId) {

    }

    @Override
    public Application getApplicationEntity(Long applicationId) {
        return null;
    }
}
