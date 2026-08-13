package com.zosh.job.mapper;

import com.zosh.job.dto.*;
import com.zosh.job.entity.Resume;
import com.zosh.job.entity.ResumeSkill;
import com.zosh.job.modal.Education;
import com.zosh.job.modal.Language;
import com.zosh.job.modal.PersonalInfo;
import com.zosh.job.modal.Project;
import com.zosh.job.modal.Certification;
import com.zosh.job.modal.Award;

import java.util.List;

public class ResumeMapper {

    public static PersonalInfoResponse toPersonalInfoResponse(PersonalInfo personalInfo) {
        if(personalInfo == null){
            return null;
        }
        return PersonalInfoResponse.builder()
                .firstName(personalInfo.getFirstName())
                .lastName(personalInfo.getLastName())
                .headline(personalInfo.getHeadline())
                .email(personalInfo.getEmail())
                .phone(personalInfo.getPhone())
                .city(personalInfo.getCity())
                .country(personalInfo.getCountry())
                .linkedinUrl(personalInfo.getLinkedinUrl())
                .githubUrl(personalInfo.getGithubUrl())
                .portfolioUrl(personalInfo.getPortfolioUrl())
                .websiteUrl(personalInfo.getWebsiteUrl())
                .build();
    }

    public static ResumeResponse toResumeResponse(Resume resume,
                                                  List<WorkExperienceResponse> workExperiences,
                                                  List<EducationResponse> educations,
                                                  List<ResumeSkillResponse> skills,
                                                  List<ProjectResponse> projects,
                                                  List<CertificationResponse> certifications,
                                                  List<AwardResponse> awards,
                                                  List<LanguageReponse> languages) {
        if (resume == null) {
            return null;
        }

        return ResumeResponse.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidateId())
                .title(resume.getTitle())
                .resumeTemplate(resume.getTemplate())
                .resumeVisibility(resume.getVisibility())
                .isDefault(resume.getIsDefault())
                .personalInfo(toPersonalInfoResponse(resume.getPersonalInfo()))
                .summary(resume.getSummary())
                .completionScore(resume.getCompletionScore())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .workExperiences(workExperiences)
                .skills(skills)
                .educations(educations)
                .projects(projects)
                .certifications(certifications)
                .awards(awards)
                .languages(languages)
                .build();
    }

    public static ResumeSkillResponse toResumeSkillResponse(ResumeSkill skill) {
        if (skill == null) {
            return null;
        }

        return ResumeSkillResponse.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .proficiencyLevel(skill.getProficiencyLevel())
                .yearsOfExperience(skill.getYearsOfExperience())
                .displayOrder(skill.getDisplayOrder())
                .build();
    }

    public static EducationResponse toEducationResponse(Education education) {
        if (education == null) {
            return null;
        }
        return EducationResponse.builder()
                .id(education.getId())
                .institutionName(education.getInstitutionName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .grade(education.getGrade())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .isCurrentlyStudying(education.getIsCurrentlyStudying())
                .description(education.getDescription())
                .displayOrder(education.getDisplayOrder())
                .build();
    }

    public static CertificationResponse toCertificationResponse(Certification certification) {
        if (certification == null) return null;
        return CertificationResponse.builder()
                .id(certification.getId())
                .certificationName(certification.getCertificationName())
                .issuingOrganization(certification.getIssuingOrganization())
                .issueDate(certification.getIssueDate())
                .expirationDate(certification.getExpirationDate())
                .displayOrder(certification.getDisplayOrder())
                .build();
    }

    public static AwardResponse toAwardResponse(Award award) {
        if (award == null) return null;
        return AwardResponse.builder()
                .id(award.getId())
                .title(award.getTitle())
                .awardDate(award.getAwardDate())
                .description(award.getDescription())
                .issueBy(award.getIssueBy())
                .displayOrder(award.getDisplayOrder())
                .build();
    }

    public static ProjectResponse toProjectResponse(Project project) {
        if (project == null) {
            return null;
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .projectUrl(project.getProjectUrl())
                .sourceCodeUrl(project.getSourceCodeUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .isOngoing(project.getIsOngoing())
                .displayOrder(project.getDisplayOrder())
                .build();
    }

    public static LanguageReponse toLanguageResponse(Language language) {
        if (language == null) {
            return null;
        }
        return LanguageReponse.builder()
                .id(language.getId())
                .languageName(language.getLanguageName())
                .languageProficiency(language.getLanguageProficiency())
                .displayOrder(language.getDisplayOrder())
                .build();
    }
}
