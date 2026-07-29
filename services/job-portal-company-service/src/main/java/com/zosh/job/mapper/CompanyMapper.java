package com.zosh.job.mapper;

import com.zosh.job.dto.SocialLinkResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.modal.Company;
import com.zosh.job.modal.SocialLink;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CompanyMapper {


    public static CompanyResponse toResponse(Company company) {

        List<SocialLinkResponse> socialLinkResponse = company.getSocialLinks() == null ? Collections.emptyList()
                : company.getSocialLinks().stream()
                .map(CompanyMapper::toSocialLinkResponse)
                .toList();

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .slug(company.getSlug())
                .tagline(company.getTagline())
                .description(company.getDescription())
                .logoUrl(company.getLogoUrl())
                .coverImageUrl(company.getCoverImageUrl())
                .website(company.getWebsite())
                .email(company.getEmail())
                .phone(company.getPhone())
                .foundedYear(company.getFoundedYear())
                .companySize(company.getCompanySize())
                .companyType(company.getCompanyType())
                .industryType(company.getIndustryType())
                .status(company.getStatus())
                .active(company.getActive())
                .ownerId(company.getOwnerId())
                .socialLinks(socialLinkResponse)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                //.verifiedAt(LocalDateTime.now())
                .isVerified(company.isVerified())
                .build();
    }

    private static SocialLinkResponse toSocialLinkResponse(SocialLink socialLink) {
        return SocialLinkResponse.builder()
                .url(socialLink.getUrl())
                .platform(socialLink.getPlatform())
                .build();
    }
}
