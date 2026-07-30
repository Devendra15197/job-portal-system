package com.zosh.job.mapper;

import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.modal.Job;
import com.zosh.job.modal.embeddable.JobLocation;
import com.zosh.job.modal.embeddable.SalaryRange;

public class JobMapper {

    public static JobResponse toJobResponse(Job job, CompanyResponse companyResponse) {
        JobLocation location = job.getLocation();
        SalaryRange sal = job.getSalaryRange();
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilites())
                .benefits(job.getBenefits())
                .company(companyResponse)
                .address(location != null ? location.getAddress() : null)
                .city(location.getCity() != null ? location.getCity(): null)
                .state(location.getState() != null ? location.getState() : null)
                .country(location.getCountry() != null ? location.getCountry() : null)
                .zipCode(location.getZipCode() != null ? location.getZipCode() : null)
                .minSalary(sal != null ? sal.getMinSalary() : null)
                .maxSalary(sal != null ? sal.getMaxSalary() : null)
                //classification
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())

                //posting
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .expiresAt(job.getExpiresAt())
                .active(job.getActive())

                //timestamps
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .publishedAt(job.getPublishedAt())
                .closedAt(job.getClosedAt())
                .build();
    }
}
