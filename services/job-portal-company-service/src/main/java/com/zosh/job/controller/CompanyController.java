package com.zosh.job.controller;

import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.CompanyRequest;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestHeader("X-User-Id") Long ownerId, @RequestBody @Valid CompanyRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(ownerId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(
            @RequestParam(required = false) CompanyType companyType,
            @RequestParam(required = false) IndustryType industryType,
            @RequestParam(required = false) CompanyStatus companyStatus
    ) {
        return ResponseEntity.ok(companyService.getAllCompanies(companyType, industryType, companyStatus));
    }

    @GetMapping("/my")
    public ResponseEntity<CompanyResponse> getMyCompany(@RequestHeader("X-User-Id") Long ownerId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getMyCompany(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long ownerId,
            @RequestBody @Valid CompanyRequest req
    ) throws Exception {
        return ResponseEntity.ok(companyService.updateCompany(id, ownerId, req));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<CompanyResponse> verifyCompany(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(companyService.verifyCompany(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CompanyResponse> deactivateCompany(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(companyService.deactivateCompany(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCompany(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long ownerId
    ) throws Exception {
        companyService.deleteCompany(id, ownerId);
        return ResponseEntity.ok(new ApiResponse("Company Deleted Successfully", true));
    }

}
