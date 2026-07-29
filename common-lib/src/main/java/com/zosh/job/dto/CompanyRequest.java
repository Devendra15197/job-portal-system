package com.zosh.job.dto;

import com.zosh.job.domain.CompanySize;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import com.zosh.job.dto.response.CompanyResponse;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String tagline;

    private String logoUrl;

    private String coverImageUrl;

    private String description;


    @Pattern(regexp = "^(https?://.*)", message = "Website must be a valid URL")
    private String website;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @Min(value = 1800, message = "Founded year cannot be before 1800")
    @Max(value = 2100, message = "Founded year cannot be in the future")
    private Integer foundedYear;

    @NotNull(message = "Company size is required")
    private CompanySize companySize;

    @NotNull(message = "Company type is required")
    private CompanyType companyType;

    @NotNull(message = "Industry type is required")
    private IndustryType industryType;

    private String registrationNumber;

    private List<SocialLinkResponse> socialLinks;

}
