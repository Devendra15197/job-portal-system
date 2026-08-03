package com.zosh.job.service.impl;

import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.CompanyRequest;
import com.zosh.job.dto.SocialLinkResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.mapper.CompanyMapper;
import com.zosh.job.modal.Company;
import com.zosh.job.modal.SocialLink;
import com.zosh.job.repository.CompanyRespository;
import com.zosh.job.service.CompanyService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRespository companyRespository;

    @Override
    public CompanyResponse createCompany(Long ownerId, CompanyRequest companyRequest) throws Exception {
        if (companyRespository.existsByOwnerId(ownerId)) {
            throw new Exception("You already have a company registered." + "Only one company per account is allowed");
        }
        if (companyRespository.existsByName(companyRequest.getName())) {
            throw new Exception("Company alreadt exists. Please choose different name.");
        }
        if (companyRequest.getRegistrationNumber() != null && companyRespository.existsByRegistrationNumber(companyRequest.getRegistrationNumber())) {
            throw new Exception("Company alreadt exists. Please choose a different registration number.");
        }
        String slug = generateUniqueSlug(companyRequest.getName());

        Company company = Company.builder()
                .name(companyRequest.getName())
                .slug(slug)
                .tagline(companyRequest.getTagline())
                .description(companyRequest.getDescription())
                .logoUrl(companyRequest.getLogoUrl())
                .coverImageUrl(companyRequest.getCoverImageUrl())
                .website(companyRequest.getWebsite())
                .email(companyRequest.getEmail())
                .phone(companyRequest.getPhone())
                .foundedYear(companyRequest.getFoundedYear())
                .companySize(companyRequest.getCompanySize())
                .companyType(companyRequest.getCompanyType())
                .industryType(companyRequest.getIndustryType())
                .registrationNumber(companyRequest.getRegistrationNumber())
                .ownerId(ownerId)
                .socialLinks(mapSocialLinks(companyRequest.getSocialLinks()))
                .build();

        Company savedCompany = companyRespository.save(company);

        return CompanyMapper.toResponse(savedCompany);
    }

    private List<SocialLink> mapSocialLinks(List<SocialLinkResponse> socialLinks) {
        if (socialLinks == null || socialLinks.isEmpty()) {
            return new ArrayList<SocialLink>();
        }

        return socialLinks.stream().map(e -> SocialLink.builder()
                .platform(e.getPlatform())
                .url(e.getUrl())
                .build()).toList();

    }

    private String generateUniqueSlug(@NotBlank(message = "Company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "").trim().replaceAll("[\\s-]+", "-");
        if (!companyRespository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (companyRespository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    @Override
    public CompanyResponse getCompanyById(Long companyId) throws Exception {
        Company company = companyRespository.findById(companyId).orElseThrow(() -> new Exception("Company now found with Id"));
        return CompanyMapper.toResponse(company);
    }

    @Override
    public CompanyResponse getMyCompany(Long ownerId) throws Exception {
        Company company = companyRespository.findByOwnerId(ownerId);
        if (company == null) {
            throw new Exception("Company not found with ownerId" + ownerId);
        }
        return CompanyMapper.toResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies(CompanyType companyType, IndustryType industryType, CompanyStatus companyStatus) {
        return companyRespository.findByFilters(companyType, industryType, companyStatus)
                .stream().map(CompanyMapper::toResponse).toList();
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest companyRequest) throws Exception {
        Company company = getCompanyEntityById(companyId);
        if (!company.getName().equals(companyRequest.getName()) && companyRespository.existsByName(companyRequest.getName())) {
            throw new Exception("Company name already exists. Please choose different name");
        }

        if (companyRequest.getRegistrationNumber() != null
                && !companyRequest.getRegistrationNumber().equals(company.getRegistrationNumber())
                && companyRespository.existsByRegistrationNumber(companyRequest.getRegistrationNumber())) {
            throw new Exception("Company registration number already exists. Please choose different registration number");
        }

        company.setName(companyRequest.getName());
        company.setTagline(companyRequest.getTagline());
        company.setDescription(companyRequest.getDescription());
        company.setLogoUrl(companyRequest.getLogoUrl());
        company.setCoverImageUrl(companyRequest.getCoverImageUrl());
        company.setWebsite(companyRequest.getWebsite());
        company.setEmail(companyRequest.getEmail());
        company.setPhone(companyRequest.getPhone());
        company.setFoundedYear(companyRequest.getFoundedYear());
        company.setCompanySize(companyRequest.getCompanySize());
        company.setCompanyType(companyRequest.getCompanyType());
        company.setIndustryType(companyRequest.getIndustryType());
        company.setRegistrationNumber(companyRequest.getRegistrationNumber());
        company.setSocialLinks(mapSocialLinks(companyRequest.getSocialLinks()));

        return CompanyMapper.toResponse(companyRespository.save(company));
    }

    @Override
    public CompanyResponse verifyCompany(Long companyId) throws Exception {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.ACTIVE);
        company.setVerified(true);
        return CompanyMapper.toResponse(companyRespository.save(company));
    }

    @Override
    public void deleteCompany(Long companyId, Long ownerId) throws Exception {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);
        companyRespository.delete(company);
    }

    private void assertOwner(Company company, Long ownerId) throws Exception {
        if (!company.getOwnerId().equals(ownerId)) {
            throw new Exception("You are not the owner of this company");
        }
    }

    @Override
    public CompanyResponse deactivateCompany(Long companyId) throws Exception {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.SUSPENDED);
        company.setVerified(false);
        return CompanyMapper.toResponse(companyRespository.save(company));
    }

    //Inter service call - not to be exposed to outside world
    @Override
    public Company getCompanyEntityById(Long companyId) throws Exception {
        Company company = companyRespository.findById(companyId).orElseThrow(() -> new Exception("Company now found with Id"));
        return company;
    }
}
