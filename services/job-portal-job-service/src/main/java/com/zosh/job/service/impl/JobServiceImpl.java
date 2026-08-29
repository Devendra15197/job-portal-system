package com.zosh.job.service.impl;

import com.zosh.job.client.CompanyClient;
import com.zosh.job.domain.JobStatus;
import com.zosh.job.dto.JobRequest;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.mapper.JobMapper;
import com.zosh.job.modal.Job;
import com.zosh.job.modal.JobCategory;
import com.zosh.job.modal.JobSkill;
import com.zosh.job.modal.JobTag;
import com.zosh.job.modal.embeddable.JobLocation;
import com.zosh.job.modal.embeddable.SalaryRange;
import com.zosh.job.payload.JobSearchRequest;
import com.zosh.job.repository.JobRepository;
import com.zosh.job.repository.JobSpecification;
import com.zosh.job.service.JobCategoryService;
import com.zosh.job.service.JobService;
import com.zosh.job.service.JobSkillService;
import com.zosh.job.service.JobTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobCategoryService jobCategoryService;
    private final JobSkillService jobSkillService;
    private final JobTagService jobTagService;
    private final CompanyClient companyClient;

    @Override
    public JobResponse createJob(Long employerId, JobRequest jobRequest) throws Exception {

        CompanyResponse companyResponse = companyClient.getMyCompany(employerId);
        JobCategory category = jobCategoryService.getJobCategoryEntityById(jobRequest.getCategoryId()); // Validate category existence

        Set<JobSkill> skills = jobRequest.getSkillIds() != null ?
                jobSkillService.getSkillByIds(jobRequest.getSkillIds()) : Collections.emptySet();

        Set<JobTag> tags = jobRequest.getTagIds() != null ?
                jobTagService.getJobTagsByIds(jobRequest.getTagIds()) : Collections.emptySet();

        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .requirements(jobRequest.getRequirements())
                .benefits(jobRequest.getBenefits())
                .companyId(companyResponse.getId())
                .employerId(employerId)
                .category(category)
                .skills(skills)
                .tags(tags)
                .location(buildLocation(jobRequest))
                .salaryRange(buildSalaryRange(jobRequest))
                .jobType(jobRequest.getJobType())
                .workMode(jobRequest.getWorkMode())
                .status(JobStatus.DRAFT)
                .experienceLevel(jobRequest.getExperienceLevel())
                .openings(jobRequest.getOpenings() != null ? jobRequest.getOpenings() : 1)
                .expiresAt(jobRequest.getExpiresAt())
                .active(true)
                .build();

        Job savedJob = jobRepository.save(job);
        return convertToResponse(savedJob, companyResponse);
    }


    @Override
    public JobResponse updateJob(Long jobId, Long employerId, JobRequest jobRequest) throws Exception {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        assertEmployer(job, employerId);

        JobCategory category = jobCategoryService.getJobCategoryEntityById(jobRequest.getCategoryId()); // Validate category existence

        Set<JobSkill> skills = jobRequest.getSkillIds() != null ?
                jobSkillService.getSkillByIds(jobRequest.getSkillIds()) : Collections.emptySet();

        Set<JobTag> tags = jobRequest.getTagIds() != null ?
                jobTagService.getJobTagsByIds(jobRequest.getTagIds()) : Collections.emptySet();


        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setRequirements(jobRequest.getRequirements());
        job.setResponsibilites(jobRequest.getResponsibilities());
        job.setBenefits(jobRequest.getBenefits());
        job.setCategory(category);
        job.setSkills(skills);
        job.setTags(tags);
        job.setLocation(buildLocation(jobRequest));
        job.setSalaryRange(buildSalaryRange(jobRequest));
        job.setJobType(jobRequest.getJobType());
        job.setWorkMode(jobRequest.getWorkMode());
        job.setExperienceLevel(jobRequest.getExperienceLevel());
        job.setOpenings(jobRequest.getOpenings() != null ? jobRequest.getOpenings() : 1);
        job.setApplicationDeadline(jobRequest.getApplicationDeadline());
        job.setExpiresAt(jobRequest.getExpiresAt());

        Job updatedJob = jobRepository.save(job);
        return convertToResponse(updatedJob);
    }

    @Override
    public JobResponse getJobById(Long id) throws Exception {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new Exception("Job not found with id: " + id));
        return convertToResponse(job);
    }

    @Override
    public List<JobResponse> getJobs(JobSearchRequest request) {
        List<Job> jobs = jobRepository.findAll(JobSpecification.build(request));
        return jobs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobResponse> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponse publishJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found with id: " + jobId));
        assertEmployer(job, employerId);
        if (job.getStatus() == JobStatus.CLOSED || job.getStatus() == JobStatus.EXPIRED) {
            throw new Exception("Cannot publish a Closed or Expired job");
        }
        job.setStatus(JobStatus.OPEN);
        job.setPublishedAt(LocalDateTime.now());
        job.setActive(true);
        Job savedJob = jobRepository.save(job);
        return convertToResponse(savedJob);
    }

    @Override
    public JobResponse closeJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found with id: " + jobId));
        assertEmployer(job, employerId);
        job.setStatus(JobStatus.CLOSED);
        job.setClosedAt(LocalDateTime.now());
        Job savedJob = jobRepository.save(job);
        return convertToResponse(savedJob);
    }

    @Override
    public void deleteJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new Exception("Job not found with id: " + jobId));
        assertEmployer(job, employerId);
        jobRepository.delete(job);
    }

    private void assertEmployer(Job job, Long employerId) throws Exception {
        if (!job.getEmployerId().equals(employerId)) {
            throw new Exception("You are not the employer who posted this job: " + employerId);
        }
    }


    @Override
    public List<JobResponse> getAllJobsAdmin() {
        return jobRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private JobResponse convertToResponse(Job savedJob) {
        CompanyResponse companyResponse = companyClient.getCompanyById(savedJob.getCompanyId());
        return convertToResponse(savedJob, companyResponse);
    }

    private JobResponse convertToResponse(Job savedJob, CompanyResponse companyResponse) {
        return JobMapper.toJobResponse(savedJob, companyResponse);
    }

    private SalaryRange buildSalaryRange(JobRequest jobRequest) {
        return SalaryRange.builder()
                .minSalary(jobRequest.getMinSalary())
                .maxSalary(jobRequest.getMaxSalary())
                .build();
    }

    private JobLocation buildLocation(JobRequest jobRequest) {

        return JobLocation.builder()
                .address(jobRequest.getAddress())
                .city(jobRequest.getCity())
                .state(jobRequest.getState())
                .country(jobRequest.getCountry())
                .zipCode(jobRequest.getZipCode())
                .build();
    }

}
