package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.client.GeminiClient;
import com.zosh.job.controller.AiApplicationController;
import com.zosh.job.controller.AiServiceController;
import com.zosh.job.controller.AiSerchController;
import com.zosh.job.controller.JobAiController;
import com.zosh.job.controller.ResumeAiController;
import com.zosh.job.payload.*;
import com.zosh.job.service.ApplicationAiService;
import com.zosh.job.service.JobAiService;
import com.zosh.job.service.ResumeAiService;
import com.zosh.job.service.SearchAiService;
import com.zosh.job.service.ScreeningScoreResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobPortalUserServiceApplicationTests {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private GeminiClient geminiClient;
    private JobAiService jobAiService;
    private ResumeAiService resumeAiService;
    private ApplicationAiService applicationAiService;
    private SearchAiService searchAiService;

    @BeforeEach
    void setUp() {
        geminiClient = mock(GeminiClient.class);
        jobAiService = mock(JobAiService.class);
        resumeAiService = mock(ResumeAiService.class);
        applicationAiService = mock(ApplicationAiService.class);
        searchAiService = mock(SearchAiService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AiServiceController(geminiClient),
                        new JobAiController(jobAiService),
                        new ResumeAiController(resumeAiService),
                        new AiApplicationController(applicationAiService),
                        new AiSerchController(searchAiService))
                .build();
    }

    @Test
    void aiServiceController_shouldReturnGeneratedText() throws Exception {
        when(geminiClient.generateText(any(), any())).thenReturn("Java is a strong choice for backend services.");

        mockMvc.perform(get("/api/ai/java"))
                .andExpect(status().isOk())
                .andExpect(content().string("Java is a strong choice for backend services."));
    }

    @Test
    void generateJobDescription_shouldReturnAiTextResponse() throws Exception {
        JobDescriptionRequest request = new JobDescriptionRequest();
        request.setTitle("Senior Java Developer");
        request.setSkills(List.of("Java", "Spring Boot", "SQL"));
        request.setExperienceLevel("Mid");
        request.setJobType("FULL_TIME");
        request.setWorkMode("REMOTE");
        request.setCategory("Engineering");

        AiTextResponse response = AiTextResponse.builder()
                .content("Detailed job description for Senior Java Developer")
                .build();

        when(jobAiService.generateJobDescription(any(JobDescriptionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/job/describe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Detailed job description for Senior Java Developer"));
    }

    @Test
    void generateSalarySuggestion_shouldReturnSalaryRangeResponse() throws Exception {
        SalaryRangeRequest request = new SalaryRangeRequest(
                "Java Developer",
                List.of("Java", "Spring Boot"),
                "Mid",
                "FULL_TIME",
                "Bengaluru"
        );

        SalaryRangeResponse response = new SalaryRangeResponse(
                "900000",
                "1500000",
                "INR",
                "YEARLY",
                "Strong demand for backend engineers in Bengaluru."
        );

        when(jobAiService.suggestSalaryRange(any(SalaryRangeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/job/salary-suggestion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minSalary").value("900000"))
                .andExpect(jsonPath("$.maxSalary").value("1500000"))
                .andExpect(jsonPath("$.currency").value("INR"));
    }

    @Test
    void generateResumeSummary_shouldReturnAiTextResponse() throws Exception {
        ResumeSummaryRequest request = new ResumeSummaryRequest();
        request.setTargetJobTitle("Senior Backend Engineer");
        request.setYearsOfExperience(5);
        request.setSkills(List.of("Java", "Spring Boot", "SQL"));

        AiTextResponse response = AiTextResponse.builder()
                .content("Results-driven backend engineer with 5 years of Java and Spring experience.")
                .build();

        when(resumeAiService.generateProfessionalSummary(any(ResumeSummaryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/resume/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Results-driven backend engineer with 5 years of Java and Spring experience."));
    }

    @Test
    void scoreCandidate_shouldReturnScreeningScoreResponse() throws Exception {
        ScreeningScoreRequest request = new ScreeningScoreRequest();
        request.setJobTitle("Backend Engineer");
        request.setExperienceLevel("Mid");
        request.setRequiredSkills(List.of("Java", "Spring Boot"));
        request.setCandidateSkills(List.of("Java", "Spring Boot", "SQL"));

        ScreeningScoreResponse response = new ScreeningScoreResponse();
        response.setScore(88);
        response.setSkillsMatchScore(92);
        response.setExperienceMatchScore(80);
        response.setEducationMatchScore(75);
        response.setMatchedSkills(List.of("Java", "Spring Boot"));
        response.setMissingSkills(List.of("Kafka"));
        response.setSummary("Strong candidate fit for the role with a few gaps in platform tooling.");

        when(applicationAiService.scoreCandidate(any(ScreeningScoreRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/application/score-candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(88))
                .andExpect(jsonPath("$.matchedSkills[0]").value("Java"));
    }

    @Test
    void enhanceSearch_shouldReturnEnhancedQueryResponse() throws Exception {
        SearchEnhanceRequest request = new SearchEnhanceRequest("Java backend developer remote");

        SearchEnhanceResponse response = new SearchEnhanceResponse(
                List.of("java", "backend", "developer"),
                List.of("remote", "bengaluru"),
                List.of("FULL_TIME"),
                List.of("REMOTE"),
                List.of("MID"),
                null,
                List.of("Java", "Spring Boot")
        );

        when(searchAiService.enhanceSearchRequest(any(SearchEnhanceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/enhance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keywords[0]").value("java"))
                .andExpect(jsonPath("$.jobTypes[0]").value("FULL_TIME"));
    }
}
