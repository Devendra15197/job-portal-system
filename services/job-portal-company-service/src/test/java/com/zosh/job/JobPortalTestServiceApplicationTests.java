package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.controller.CompanyController;
import com.zosh.job.domain.CompanySize;
import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.CompanyRequest;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.modal.Company;
import com.zosh.job.service.CompanyService;
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

class JobPortalCompanyServiceApplicationTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FakeCompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new FakeCompanyService();
        mockMvc = MockMvcBuilders.standaloneSetup(new CompanyController(companyService)).build();
    }

    @Test
    void createCompanyReturnsCreatedCompany() throws Exception {
        CompanyRequest request = companyRequest();
        companyService.companyResponse = companyResponse();

        mockMvc.perform(post("/api/companies")
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Acme Technologies"))
                .andExpect(jsonPath("$.ownerId").value(10L));

        assertEquals(10L, companyService.ownerId);
        assertEquals("Acme Technologies", companyService.companyRequest.getName());
    }

    @Test
    void getCompanyByIdReturnsCompany() throws Exception {
        companyService.companyResponse = companyResponse();

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slug").value("acme-technologies"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        assertEquals(1L, companyService.companyId);
    }

    @Test
    void getAllCompaniesPassesFiltersAndReturnsCompanies() throws Exception {
        companyService.companyResponses = List.of(companyResponse());

        mockMvc.perform(get("/api/companies")
                        .param("companyType", "PRIVATE")
                        .param("industryType", "TECHNOLOGY")
                        .param("companyStatus", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("hr@acme.example"));

        assertEquals(CompanyType.PRIVATE, companyService.companyType);
        assertEquals(IndustryType.TECHNOLOGY, companyService.industryType);
        assertEquals(CompanyStatus.ACTIVE, companyService.companyStatus);
    }

    @Test
    void getMyCompanyUsesOwnerHeader() throws Exception {
        companyService.companyResponse = companyResponse();

        mockMvc.perform(get("/api/companies/my")
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(10L))
                .andExpect(jsonPath("$.name").value("Acme Technologies"));

        assertEquals(10L, companyService.ownerId);
    }

    @Test
    void updateCompanyReturnsUpdatedCompany() throws Exception {
        CompanyRequest request = companyRequest();
        request.setName("Acme Labs");

        CompanyResponse response = companyResponse();
        response.setName("Acme Labs");

        companyService.companyResponse = response;

        mockMvc.perform(put("/api/companies/{id}", 1L)
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Acme Labs"));

        assertEquals(1L, companyService.companyId);
        assertEquals(10L, companyService.ownerId);
        assertEquals("Acme Labs", companyService.companyRequest.getName());
    }

    @Test
    void verifyCompanyReturnsVerifiedCompany() throws Exception {
        CompanyResponse response = companyResponse();
        response.setIsVerified(true);
        response.setStatus(CompanyStatus.ACTIVE);

        companyService.companyResponse = response;

        mockMvc.perform(patch("/api/companies/{id}/verify", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.isVerified").value(true));

        assertEquals(1L, companyService.companyId);
    }

    @Test
    void deactivateCompanyReturnsSuspendedCompany() throws Exception {
        CompanyResponse response = companyResponse();
        response.setIsVerified(false);
        response.setStatus(CompanyStatus.SUSPENDED);

        companyService.companyResponse = response;

        mockMvc.perform(patch("/api/companies/{id}/deactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"))
                .andExpect(jsonPath("$.isVerified").value(false));

        assertEquals(1L, companyService.companyId);
    }

    @Test
    void deleteCompanyReturnsApiResponse() throws Exception {
        mockMvc.perform(delete("/api/companies/{id}", 1L)
                        .header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Company Deleted Successfully"))
                .andExpect(jsonPath("$.status").value(true));

        assertEquals(1L, companyService.companyId);
        assertEquals(10L, companyService.ownerId);
    }

    private CompanyRequest companyRequest() {
        return CompanyRequest.builder()
                .name("Acme Technologies")
                .tagline("Hiring made simple")
                .description("A software product company")
                .website("https://acme.example")
                .email("hr@acme.example")
                .phone("9876543210")
                .foundedYear(2020)
                .companySize(CompanySize.MEDIUM)
                .companyType(CompanyType.PRIVATE)
                .industryType(IndustryType.TECHNOLOGY)
                .registrationNumber("REG-123")
                .build();
    }

    private CompanyResponse companyResponse() {
        return CompanyResponse.builder()
                .id(1L)
                .name("Acme Technologies")
                .slug("acme-technologies")
                .tagline("Hiring made simple")
                .description("A software product company")
                .website("https://acme.example")
                .email("hr@acme.example")
                .phone("9876543210")
                .foundedYear(2020)
                .companySize(CompanySize.MEDIUM)
                .companyType(CompanyType.PRIVATE)
                .industryType(IndustryType.TECHNOLOGY)
                .status(CompanyStatus.ACTIVE)
                .active(true)
                .ownerId(10L)
                .isVerified(false)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();
    }

    private static class FakeCompanyService implements CompanyService {
        private Long ownerId;
        private Long companyId;
        private CompanyType companyType;
        private IndustryType industryType;
        private CompanyStatus companyStatus;
        private CompanyRequest companyRequest;
        private CompanyResponse companyResponse;
        private List<CompanyResponse> companyResponses = List.of();

        @Override
        public CompanyResponse createCompany(Long ownerId, CompanyRequest companyRequest) {
            this.ownerId = ownerId;
            this.companyRequest = companyRequest;
            return companyResponse;
        }

        @Override
        public CompanyResponse getCompanyById(Long companyId) {
            this.companyId = companyId;
            return companyResponse;
        }

        @Override
        public CompanyResponse getMyCompany(Long userId) {
            this.ownerId = userId;
            return companyResponse;
        }

        @Override
        public List<CompanyResponse> getAllCompanies(
                CompanyType companyType,
                IndustryType industryType,
                CompanyStatus companyStatus
        ) {
            this.companyType = companyType;
            this.industryType = industryType;
            this.companyStatus = companyStatus;
            return companyResponses;
        }

        @Override
        public CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest companyRequest) {
            this.companyId = companyId;
            this.ownerId = ownerId;
            this.companyRequest = companyRequest;
            return companyResponse;
        }

        @Override
        public CompanyResponse verifyCompany(Long companyId) {
            this.companyId = companyId;
            return companyResponse;
        }

        @Override
        public void deleteCompany(Long companyId, Long ownerId) {
            this.companyId = companyId;
            this.ownerId = ownerId;
        }

        @Override
        public CompanyResponse deactivateCompany(Long companyId) {
            this.companyId = companyId;
            return companyResponse;
        }

        @Override
        public Company getCompanyEntityById(Long companyId) {
            this.companyId = companyId;
            return null;
        }
    }
}
