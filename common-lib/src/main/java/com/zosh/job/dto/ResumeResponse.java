package com.zosh.job.dto;

import com.zosh.job.domain.ResumeTemplate;
import com.zosh.job.domain.ResumeVisibility;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {
    private Long id;
    private Long candidateId;
    private ResumeTemplate resumeTemplate;
    private ResumeVisibility resumeVisibility;
    private Boolean isDefault;
    private String title;
    private PersonalInfoResponse personalInfo;
    private String summary;
    //    private String uploadedFileName;
//    private String uploadedFileUrl;
    private Integer completionScore;
    //    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private LocalDateTime lastViewedAt;

    private List<WorkExperienceResponse> workExperiences;
    private List<EducationResponse> educations;
    private List<ResumeSkillResponse> skills;
    private List<ProjectResponse> projects;
    private List<CertificationResponse> certifications;
    private List<AwardResponse> awards;
    private List<LanguageReponse> languages;
}
