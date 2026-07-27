package com.zosh.job.modal;

import com.zosh.job.domain.CompanySize;
import com.zosh.job.domain.CompanyStatus;
import com.zosh.job.domain.CompanyType;
import com.zosh.job.domain.IndustryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    private String tagline;

    private String description;

    private String logoUrl;

    private String coverImageUrl;

    private String website;

    private Integer foundedYear;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status = CompanyStatus.ACTIVE;

    @Column(unique = true)
    private String registrationNumber;

    @Column(nullable = false, unique = true)
    private Long ownerId;

    @ElementCollection
    private List<SocialLink> socialLinks = new ArrayList<>();
}
