package com.zosh.job.service;

import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.CompanyRequest;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.modal.Company;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest companyRequest) throws Exception;

    CompanyResponse getCompanyById(Long companyId) throws Exception;

    CompanyResponse getMyCompany(Long userId) throws Exception;

    List<CompanyResponse> getAllCompanies(CompanyType companyType, IndustryType industryType, CompanyStatus companyStatus);

    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest companyRequest) throws Exception;

    CompanyResponse verifyCompany(Long companyId) throws Exception;

    void deleteCompany(Long companyId, Long ownerId) throws Exception;

    CompanyResponse deactivateCompany(Long companyId) throws Exception;

    //Inter service call
    Company getCompanyEntityById(Long companyId) throws Exception;
}