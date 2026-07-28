package com.zosh.job.service;

import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.CompanyRequest;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.modal.Company;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(CompanyRequest companyRequest);

    CompanyResponse getCompanyById(Long companyId);

    CompanyResponse getMyCompany(Long userId);

    List<CompanyResponse> getAllCompanies(CompanyType companyType, IndustryType industryType, CompanyStatus companyStatus);

    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest companyRequest);

    CompanyResponse verifyCompany(Long companyId);

    void deleteCompany(Long companyId);

    CompanyResponse deactivateCompany(Long companyId);

    //Inter service call
    Company getCompanyEntityById(Long companyId);
}