package com.zosh.job.mapper;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.ApplicationNoteResponse;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.modal.Application;
import com.zosh.job.modal.ApplicationNote;
import com.zosh.job.payload.CreateApplicationRequest;

import java.util.List;

public class ApplicationMapper {

    public static Application toEntity(CreateApplicationRequest request, Long candidateId, Long companyId, Long employerId) {
        if (request == null) return null;

        return Application.builder()
                .candidateId(candidateId)
                .jobId(request.getJobId())
                .companyId(companyId)
                .employerId(employerId)
                .resumeId(request.getResumeId())
                .status(ApplicationStatus.PENDING)
                .coverLetter(request.getCoverLetter())
                .expectedSalary(request.getExpectedSalary())
                .availableFrom(request.getAvailableFrom())
                .build();

    }

    public static ApplicationResponse toResponse(Application application, List<ApplicationNote> notes, JobResponse job, CompanyResponse company, UserResponse candidate) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .candidate(candidate)
                .employerId(application.getEmployerId())
                .job(job)
                .company(company)
                .status(application.getStatus())
                .resumeId(application.getResumeId())
                .coverLetter(application.getCoverLetter())
                .expectedSalary(application.getExpectedSalary())
                .availableFrom(application.getAvailableFrom())
                .isStarred(application.getIsStarred())
                .notes(notes.stream().map(ApplicationMapper::toNoteResponse).toList())
                .withdrawnAt(application.getWithdrawnAt())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())

                .build();
    }

    public static ApplicationNoteResponse toNoteResponse(ApplicationNote note) {
        return ApplicationNoteResponse.builder()
                .id(note.getId())
                .content(note.getContent())
                .addedByUserId(note.getAddedByUserId())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
