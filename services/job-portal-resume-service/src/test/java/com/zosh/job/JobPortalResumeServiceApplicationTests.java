package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.controller.ResumeController;
import com.zosh.job.domain.ResumeTemplate;
import com.zosh.job.domain.ResumeVisibility;
import com.zosh.job.dto.PersonalInfoResponse;
import com.zosh.job.dto.ResumeResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.payload.CreateResumeRequest;
import com.zosh.job.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobPortalResumeServiceApplicationTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FakeResumeService resumeService;

    @BeforeEach
    void setUp() {
        resumeService = new FakeResumeService();
        mockMvc = MockMvcBuilders.standaloneSetup(new ResumeController(resumeService)).build();
    }

    @Test
    void createResumeReturnsCreatedResume() throws Exception {
        CreateResumeRequest request = CreateResumeRequest.builder()
                .title("Backend Developer Resume")
                .template(ResumeTemplate.PROFESSIONAL)
                .visibility(ResumeVisibility.PUBLIC)
                .isDefault(true)
                .build();
        resumeService.resumeResponse = resumeResponse();

        mockMvc.perform(post("/api/resumes")
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.candidateId").value(10L))
                .andExpect(jsonPath("$.title").value("Backend Developer Resume"))
                .andExpect(jsonPath("$.resumeTemplate").value("PROFESSIONAL"))
                .andExpect(jsonPath("$.resumeVisibility").value("PUBLIC"))
                .andExpect(jsonPath("$.isDefault").value(true));

        assertEquals(10L, resumeService.candidateId);
        assertEquals("Backend Developer Resume", resumeService.createResumeRequest.getTitle());
    }

    @Test
    void getResumeByIdReturnsResume() throws Exception {
        resumeService.resumeResponse = resumeResponse();

        mockMvc.perform(get("/api/resumes/{resumeId}", 1L)
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.candidateId").value(10L))
                .andExpect(jsonPath("$.personalInfo.email").value("aarav.sharma@example.com"));

        assertEquals(1L, resumeService.resumeId);
        assertEquals(10L, resumeService.candidateId);
    }

    @Test
    void getMyResumesReturnsCandidateResumes() throws Exception {
        resumeService.resumeResponses = List.of(resumeResponse());

        mockMvc.perform(get("/api/resumes/my")
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Backend Developer Resume"));

        assertEquals(10L, resumeService.candidateId);
    }

    @Test
    void updatePersonalInfoReturnsUpdatedResume() throws Exception {
        PersonalInfoResponse request = personalInfoResponse();
        request.setCity("Pune");

        ResumeResponse response = resumeResponse();
        response.getPersonalInfo().setCity("Pune");
        resumeService.resumeResponse = response;

        mockMvc.perform(put("/api/resumes/{resumeId}/personal-info", 1L)
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalInfo.city").value("Pune"))
                .andExpect(jsonPath("$.personalInfo.firstName").value("Aarav"));

        assertEquals(1L, resumeService.resumeId);
        assertEquals(10L, resumeService.candidateId);
        assertEquals("Pune", resumeService.personalInfoRequest.getCity());
    }

    @Test
    void updateSummaryReturnsUpdatedResume() throws Exception {
        ResumeResponse response = resumeResponse();
        response.setSummary("Updated backend engineer summary");
        resumeService.resumeResponse = response;

        mockMvc.perform(patch("/api/resumes/{resumeId}/summary", 1L)
                        .header("X-User-Id", 10L)
                        .param("summary", "Updated backend engineer summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").value("Updated backend engineer summary"));

        assertEquals(1L, resumeService.resumeId);
        assertEquals(10L, resumeService.candidateId);
        assertEquals("Updated backend engineer summary", resumeService.summary);
    }

    @Test
    void setDefaultResumeReturnsDefaultResume() throws Exception {
        ResumeResponse response = resumeResponse();
        response.setIsDefault(true);
        resumeService.resumeResponse = response;

        mockMvc.perform(patch("/api/resumes/{resumeId}/set-default", 1L)
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDefault").value(true));

        assertEquals(1L, resumeService.resumeId);
        assertEquals(10L, resumeService.candidateId);
    }

    @Test
    void deleteResumeReturnsApiResponse() throws Exception {
        mockMvc.perform(delete("/api/resumes/{resumeId}", 1L)
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Resume Deleted Successfully"))
                .andExpect(jsonPath("$.status").value(true));

        assertEquals(1L, resumeService.resumeId);
        assertEquals(10L, resumeService.candidateId);
        assertEquals(true, resumeService.deleted);
    }

    private ResumeResponse resumeResponse() {
        return ResumeResponse.builder()
                .id(1L)
                .candidateId(10L)
                .title("Backend Developer Resume")
                .resumeTemplate(ResumeTemplate.PROFESSIONAL)
                .resumeVisibility(ResumeVisibility.PUBLIC)
                .isDefault(true)
                .personalInfo(personalInfoResponse())
                .summary("Java backend engineer with Spring Boot experience")
                .completionScore(80)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 1, 2, 10, 0))
                .workExperiences(List.of())
                .educations(List.of())
                .skills(List.of())
                .projects(List.of())
                .certifications(List.of())
                .awards(List.of())
                .languages(List.of())
                .build();
    }

    private PersonalInfoResponse personalInfoResponse() {
        return PersonalInfoResponse.builder()
                .firstName("Aarav")
                .lastName("Sharma")
                .headline("Backend Developer")
                .email("aarav.sharma@example.com")
                .phone("9876543210")
                .city("Mumbai")
                .country("India")
                .linkedinUrl("https://linkedin.com/in/aarav")
                .githubUrl("https://github.com/aarav")
                .portfolioUrl("https://aarav.dev")
                .websiteUrl("https://example.com")
                .build();
    }

    private static class FakeResumeService implements ResumeService {
        private Long resumeId;
        private Long candidateId;
        private String summary;
        private boolean deleted;
        private ResumeResponse resumeResponse;
        private List<ResumeResponse> resumeResponses = List.of();
        private CreateResumeRequest createResumeRequest;
        private PersonalInfoResponse personalInfoRequest;

        @Override
        public ResumeResponse createResume(Long candidateId, CreateResumeRequest resumeRequest) {
            this.candidateId = candidateId;
            this.createResumeRequest = resumeRequest;
            return resumeResponse;
        }

        @Override
        public ResumeResponse getResumeById(Long resumeId, Long candidateId) {
            this.resumeId = resumeId;
            this.candidateId = candidateId;
            return resumeResponse;
        }

        @Override
        public List<ResumeResponse> getMyResumes(Long candidateId) {
            this.candidateId = candidateId;
            return resumeResponses;
        }

        @Override
        public ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfoResponse req) {
            this.resumeId = resumeId;
            this.candidateId = candidateId;
            this.personalInfoRequest = req;
            return resumeResponse;
        }

        @Override
        public ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) {
            this.resumeId = resumeId;
            this.candidateId = candidateId;
            this.summary = summary;
            return resumeResponse;
        }

        @Override
        public ResumeResponse setDefaultResume(Long resumeId, Long candidateId) {
            this.resumeId = resumeId;
            this.candidateId = candidateId;
            return resumeResponse;
        }

        @Override
        public void deleteResume(Long resumeId, Long candidateId) {
            this.resumeId = resumeId;
            this.candidateId = candidateId;
            this.deleted = true;
        }

        @Override
        public Resume getResumeEntity(Long resumeId) {
            this.resumeId = resumeId;
            return null;
        }
    }
}
