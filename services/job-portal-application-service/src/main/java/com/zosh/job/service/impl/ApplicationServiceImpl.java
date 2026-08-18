package com.zosh.job.service.impl;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.mapper.ApplicationMapper;
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
    public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) throws Exception {
        if (repository.existsByCandidateIdAndJobId(candidateId, request.getJobId())) {
            throw new Exception("You have already applied to this application");
        }

        Long companyId = 1L;
        Long employerId = 1L;
        //fetch job
        //fetch resume
        Application application = ApplicationMapper.toEntity(request, candidateId, companyId, employerId);
        Application savedApplication = repository.save(application);

        //todo: AI Screening
        return buildFullResponse(savedApplication);

    }

    @Override
    public ApplicationResponse getApplicationById(Long applicationId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        return buildFullResponse(application);
    }

    @Override
    public List<ApplicationResponse> getApplications(Long candidateId) {
        return repository.findByCandidateId(candidateId).stream()
                .map(this::buildFullResponse).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return repository.findByJobId(jobId).stream()
                .map(this::buildFullResponse).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCompany(Long companyId) {
        return null;
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
    public Application getApplicationEntity(Long applicationId) throws Exception {
        return repository.findById(applicationId).orElseThrow(() -> new Exception("application not found"));
    }

    public ApplicationResponse buildFullResponse(Application application) {

        //TODO
        JobResponse job = JobResponse.builder().id(application.getJobId()).build();
        CompanyResponse company = CompanyResponse.builder().id(application.getCompanyId()).build();
        UserResponse candidate = UserResponse.builder().id(application.getCandidateId()).build();
        return ApplicationMapper.toResponse(application, job, company, candidate);
    }
}
