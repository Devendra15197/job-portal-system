package com.zosh.job.mapper;

import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.modal.Application;
import com.zosh.job.payload.CreateApplicationRequest;

public class ApplicationMapper {

    public static Application toEntity(CreateApplicationRequest request, Long candidateId, Long companyId, Long employerId) {
        if (request == null) return null;

        return Application.builder()
                .candidateId(candidateId)
                .jobId(request.getJobId())
                .companyId(companyId)
                .employerId(employerId)
                .resumeId(request.getResumeId())
                .coverLetter(request.getCoverLetter())
                .expectedSalary(request.getExpectedSalary())
                .availableFrom(request.getAvailableFrom())
                .build();

    }

    public static ApplicationResponse toResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .build();
    }
}
