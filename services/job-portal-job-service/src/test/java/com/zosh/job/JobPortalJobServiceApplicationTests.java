package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.controller.JobController;
import com.zosh.job.domain.ExperienceLevel;
import com.zosh.job.domain.JobStatus;
import com.zosh.job.domain.JobType;
import com.zosh.job.domain.WorkMode;
import com.zosh.job.dto.JobCategoryResponse;
import com.zosh.job.dto.JobRequest;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.JobSkillResponse;
import com.zosh.job.payload.JobSearchRequest;
import com.zosh.job.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobPortalJobServiceApplicationTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FakeJobService jobService;

    @BeforeEach
    void setUp() {
        jobService = new FakeJobService();
        mockMvc = MockMvcBuilders.standaloneSetup(new JobController(jobService)).build();
    }

    @Test
    void createJobReturnsCreatedJob() throws Exception {
        JobRequest request = jobRequest();
        jobService.jobResponse = jobResponse();

        mockMvc.perform(post("/api/jobs")
                        .header("X-User-Id", 20L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Java Backend Developer"))
                .andExpect(jsonPath("$.employerId").value(20L))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        assertEquals(20L, jobService.employerId);
        assertEquals("Java Backend Developer", jobService.jobRequest.getTitle());
    }

    @Test
    void getJobByIdReturnsJob() throws Exception {
        jobService.jobResponse = jobResponse();

        mockMvc.perform(get("/api/jobs/{jobId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Java Backend Developer"))
                .andExpect(jsonPath("$.category.name").value("Software Development"));

        assertEquals(1L, jobService.jobId);
    }

    @Test
    void getAllJobsReturnsFilteredJobs() throws Exception {
        jobService.jobResponses = List.of(jobResponse());

        mockMvc.perform(get("/api/jobs")
                        .param("keyword", "java")
                        .param("companyId", "5")
                        .param("location", "Pune")
                        .param("jobType", "FULL_TIME")
                        .param("workMode", "HYBRID")
                        .param("experienceLevel", "MID_LEVEL")
                        .param("status", "DRAFT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Java Backend Developer"));

        assertEquals("java", jobService.searchRequest.getKeyword());
        assertEquals(5L, jobService.searchRequest.getCompanyId());
        assertEquals(JobType.FULL_TIME, jobService.searchRequest.getJobType());
        assertEquals(WorkMode.HYBRID, jobService.searchRequest.getWorkMode());
        assertEquals(ExperienceLevel.MID_LEVEL, jobService.searchRequest.getExperienceLevel());
        assertEquals(JobStatus.DRAFT, jobService.searchRequest.getStatus());
    }

    @Test
    void getJobsByCompanyReturnsCompanyJobs() throws Exception {
        jobService.jobResponses = List.of(jobResponse());

        mockMvc.perform(get("/api/jobs/company/{companyId}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employerId").value(20L));

        assertEquals(5L, jobService.companyId);
    }

    @Test
    void getAllJobsAdminReturnsJobs() throws Exception {
        jobService.jobResponses = List.of(jobResponse());

        mockMvc.perform(get("/api/jobs/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void updateJobReturnsUpdatedJob() throws Exception {
        JobRequest request = jobRequest();
        request.setTitle("Senior Java Backend Developer");

        JobResponse response = jobResponse();
        response.setTitle("Senior Java Backend Developer");
        jobService.jobResponse = response;

        mockMvc.perform(put("/api/jobs/{jobId}", 1L)
                        .header("X-User-Id", 20L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Java Backend Developer"));

        assertEquals(1L, jobService.jobId);
        assertEquals(20L, jobService.employerId);
        assertEquals("Senior Java Backend Developer", jobService.jobRequest.getTitle());
    }

    @Test
    void publishJobReturnsOpenJob() throws Exception {
        JobResponse response = jobResponse();
        response.setStatus(JobStatus.OPEN);
        jobService.jobResponse = response;

        mockMvc.perform(patch("/api/jobs/{jobId}/publish", 1L)
                        .header("X-User-Id", 20L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));

        assertEquals(1L, jobService.jobId);
        assertEquals(20L, jobService.employerId);
    }

    @Test
    void closeJobReturnsClosedJob() throws Exception {
        JobResponse response = jobResponse();
        response.setStatus(JobStatus.CLOSED);
        jobService.jobResponse = response;

        mockMvc.perform(patch("/api/jobs/{jobId}/close", 1L)
                        .header("X-User-Id", 20L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));

        assertEquals(1L, jobService.jobId);
        assertEquals(20L, jobService.employerId);
    }

    @Test
    void deleteJobReturnsApiResponse() throws Exception {
        mockMvc.perform(delete("/api/jobs/{jobId}", 1L)
                        .header("X-User-Id", 20L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Job deleted successfully"))
                .andExpect(jsonPath("$.status").value(true));

        assertEquals(1L, jobService.jobId);
        assertEquals(20L, jobService.employerId);
        assertEquals(true, jobService.deleted);
    }

    private JobRequest jobRequest() {
        return JobRequest.builder()
                .title("Java Backend Developer")
                .description("Build and maintain Spring Boot services")
                .requirements("Java, Spring Boot, REST APIs")
                .responsibilities("Design APIs and review code")
                .benefits("Health insurance")
                .categoryId(2L)
                .skillIds(Set.of(101L))
                .tagIds(Set.of(201L))
                .address("Baner Road")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .zipCode("411045")
                .minSalary(BigDecimal.valueOf(800000))
                .maxSalary(BigDecimal.valueOf(1400000))
                .jobType(JobType.FULL_TIME)
                .workMode(WorkMode.HYBRID)
                .experienceLevel(ExperienceLevel.MID_LEVEL)
                .openings(3)
                .build();
    }

    private JobResponse jobResponse() {
        return JobResponse.builder()
                .id(1L)
                .title("Java Backend Developer")
                .description("Build and maintain Spring Boot services")
                .requirements("Java, Spring Boot, REST APIs")
                .responsibilities("Design APIs and review code")
                .benefits("Health insurance")
                .employerId(20L)
                .category(JobCategoryResponse.builder()
                        .id(2L)
                        .name("Software Development")
                        .slug("software-development")
                        .active(true)
                        .build())
                .skills(Set.of(JobSkillResponse.builder()
                        .id(101L)
                        .name("Java")
                        .slug("java")
                        .active(true)
                        .build()))
                .address("Baner Road")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .zipCode("411045")
                .minSalary(BigDecimal.valueOf(800000))
                .maxSalary(BigDecimal.valueOf(1400000))
                .currency("INR")
                .salaryNegotiable(true)
                .salaryDisclosed(true)
                .jobType(JobType.FULL_TIME)
                .workMode(WorkMode.HYBRID)
                .experienceLevel(ExperienceLevel.MID_LEVEL)
                .status(JobStatus.DRAFT)
                .openings(3)
                .applicationDeadline(LocalDate.of(2026, 12, 31))
                .expiresAt(LocalDate.of(2027, 1, 31))
                .active(true)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();
    }

    private static class FakeJobService implements JobService {
        private Long jobId;
        private Long employerId;
        private Long companyId;
        private boolean deleted;
        private JobRequest jobRequest;
        private JobSearchRequest searchRequest;
        private JobResponse jobResponse;
        private List<JobResponse> jobResponses = List.of();

        @Override
        public JobResponse createJob(Long employerId, JobRequest jobRequest) {
            this.employerId = employerId;
            this.jobRequest = jobRequest;
            return jobResponse;
        }

        @Override
        public JobResponse updateJob(Long jobId, Long employerId, JobRequest jobRequest) {
            this.jobId = jobId;
            this.employerId = employerId;
            this.jobRequest = jobRequest;
            return jobResponse;
        }

        @Override
        public JobResponse getJobById(Long id) {
            this.jobId = id;
            return jobResponse;
        }

        @Override
        public List<JobResponse> getJobs(JobSearchRequest request) {
            this.searchRequest = request;
            return jobResponses;
        }

        @Override
        public List<JobResponse> getJobsByCompany(Long companyId) {
            this.companyId = companyId;
            return jobResponses;
        }

        @Override
        public JobResponse publishJob(Long jobId, Long employerId) {
            this.jobId = jobId;
            this.employerId = employerId;
            return jobResponse;
        }

        @Override
        public JobResponse closeJob(Long jobId, Long employerId) {
            this.jobId = jobId;
            this.employerId = employerId;
            return jobResponse;
        }

        @Override
        public void deleteJob(Long jobId, Long employerId) {
            this.jobId = jobId;
            this.employerId = employerId;
            this.deleted = true;
        }

        @Override
        public List<JobResponse> getAllJobsAdmin() {
            return jobResponses;
        }
    }
}
