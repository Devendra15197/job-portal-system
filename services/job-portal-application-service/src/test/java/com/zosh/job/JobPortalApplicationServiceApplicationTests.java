package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.controller.ApplicationController;
import com.zosh.job.controller.ApplicationNoteController;
import com.zosh.job.controller.HomeController;
import com.zosh.job.dto.ApplicationResponse;
import com.zosh.job.payload.CreateApplicationRequest;
import com.zosh.job.service.ApplicationNoteService;
import com.zosh.job.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobPortalApplicationServiceApplicationTests {

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private FakeApplicationService applicationService;
	private FakeApplicationNoteService applicationNoteService;

	@BeforeEach
	void setUp() {
		applicationService = new FakeApplicationService();
		applicationNoteService = new FakeApplicationNoteService();

		mockMvc = MockMvcBuilders.standaloneSetup(
						new HomeController(),
						new ApplicationController(applicationService),
						new ApplicationNoteController(applicationNoteService))
				.build();
	}

	@Test
	void welcomeEndpointReturnsMessage() throws Exception {
		mockMvc.perform(get("/api/v1/home/welcome"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Welcome to the APPLICATION Portal Company Service!"));
	}

	@Test
	void getApplicationByIdReturnsApplication() throws Exception {
		ApplicationResponse resp = ApplicationResponse.builder().id(1L).build();
		applicationService.getApplicationByIdResponse = resp;

		mockMvc.perform(get("/api/applications/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void createApplicationDelegatesToService() throws Exception {
		CreateApplicationRequest req = CreateApplicationRequest.builder()
				.jobId(100L)
				.resumeId(200L)
				.build();

		ApplicationResponse resp = ApplicationResponse.builder().id(10L).build();
		applicationService.createApplicationResponse = resp;

		mockMvc.perform(post("/api/applications")
						.header("X-User-Id", 42L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10));

		org.junit.jupiter.api.Assertions.assertEquals(42L, applicationService.createCandidateId);
		org.junit.jupiter.api.Assertions.assertEquals(100L, applicationService.createRequest.getJobId());
	}

	@Test
	void getMyApplicationsReturnsList() throws Exception {
		ApplicationResponse resp = ApplicationResponse.builder().id(5L).build();
		applicationService.getApplicationsResponse = List.of(resp);

		mockMvc.perform(get("/api/applications/my").header("X-User-Id", 123L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(5));

		org.junit.jupiter.api.Assertions.assertEquals(123L, applicationService.getApplicationsCandidateId);
	}

	private static class FakeApplicationService implements ApplicationService {
		private Long createCandidateId;
		private CreateApplicationRequest createRequest;
		private ApplicationResponse createApplicationResponse;

		private ApplicationResponse getApplicationByIdResponse;

		private Long getApplicationsCandidateId;
		private List<ApplicationResponse> getApplicationsResponse = List.of();

		@Override
		public ApplicationResponse createApplication(Long candidateId, CreateApplicationRequest request) throws Exception {
			this.createCandidateId = candidateId;
			this.createRequest = request;
			return createApplicationResponse;
		}

		@Override
		public ApplicationResponse getApplicationById(Long applicationId) throws Exception {
			return getApplicationByIdResponse;
		}

		@Override
		public List<ApplicationResponse> getApplications(Long candidateId) {
			this.getApplicationsCandidateId = candidateId;
			return getApplicationsResponse;
		}

		@Override
		public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
			return List.of();
		}

		@Override
		public List<ApplicationResponse> getApplicationsForCompany(Long userId, com.zosh.job.payload.CompanyApplicationFilterRequest request) {
			return List.of();
		}

		@Override
		public ApplicationResponse updateStatus(Long applicationId, Long employerId, com.zosh.job.domain.ApplicationStatus status) throws Exception {
			return null;
		}

		@Override
		public ApplicationResponse withdraw(Long applicationId, Long candidateId, com.zosh.job.payload.WithdrawApplicationRequest request) throws Exception {
			return null;
		}

		@Override
		public ApplicationResponse toggleStar(Long applicationId, Long employerId) throws Exception {
			return null;
		}

		@Override
		public void deleteApplication(Long applicationId, Long candidateId) throws Exception {

		}

		@Override
		public com.zosh.job.modal.Application getApplicationEntity(Long applicationId) throws Exception {
			return null;
		}
	}

	private static class FakeApplicationNoteService implements ApplicationNoteService {
		@Override
		public com.zosh.job.dto.ApplicationNoteResponse addNote(Long applicationId, Long employerId, com.zosh.job.payload.AddApplicationNoteRequest request) throws Exception {
			return null;
		}

		@Override
		public List<com.zosh.job.dto.ApplicationNoteResponse> getNoteByApplication(Long applicationId, Long employerId) {
			return List.of();
		}

		@Override
		public void deleteNote(Long applicationId, Long noteId, Long employerId) throws Exception {

		}
	}

}


