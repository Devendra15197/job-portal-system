package com.zosh.job.service.impl;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.mapper.ApplicationMapper;
import com.zosh.job.modal.Application;
import com.zosh.job.payload.CompanyApplicationFilterRequest;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.payload.WithdrawApplicationRequest;
import com.zosh.job.repository.ApplicationRepository;
import com.zosh.job.repository.ApplicationSpecification;
import com.zosh.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public List<ApplicationResponse> getApplicationsForCompany(Long userId, CompanyApplicationFilterRequest filter) {

        //TODO: Fetch company
        Long companyId = 1L;

        Sort sort = buildSort(filter.getSortBy());
        return repository.findAll(ApplicationSpecification.forCompanyWithFilters(
                        companyId,
                        filter.getJobId(),
                        filter.getStatus(),
                        filter.getIsStarred(),
                        filter.getAiShortListStatus(),
                        filter.getMinAiScore()
                ), sort).stream()
                .map(this::buildFullResponse).toList();
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, Long employerId, ApplicationStatus status) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new Exception("You cannot update the status of a withdrawn application");
        }

        application.setStatus(status);
        Application savedApplication = repository.save(application);
        return buildFullResponse(savedApplication);
    }


    @Override
    public ApplicationResponse withdraw(Long applicationId, Long candidateId, WithdrawApplicationRequest request) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawnReason(request.getReason());
        Application savedApplication = repository.save(application);
        return buildFullResponse(savedApplication);
    }


    @Override
    public ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        application.setIsStarred(!application.getIsStarred());
        Application savedApplication = repository.save(application);
        return buildFullResponse(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId, Long candidateId) throws Exception {
        Application application = getApplicationEntity(applicationId);
        assertCandidate(application, candidateId);
        repository.delete(application);
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


    private Sort buildSort(String sortBy) {
        if ("AI_SCORE_DESC".equals(sortBy)) {
            return Sort.by(Sort.Order.desc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        } else if ("AI_SCORE_ASC".equals(sortBy)) {
            return Sort.by(Sort.Order.asc("aiScore").with(Sort.NullHandling.NULLS_LAST));
        }
        return Sort.by(Sort.Direction.DESC, "appliedAt");
    }


    private void assertEmployer(Application application, Long employerId) throws Exception {
        if (!application.getEmployerId().equals(employerId)) {
            throw new Exception("You are not authorized to update this application");
        }
    }


    private void assertCandidate(Application application, Long candidateId) throws Exception {
        if (!application.getCandidateId().equals(candidateId)) {
            throw new Exception("You are not authorized to withdraw this application");
        }
    }

}
