package com.zosh.job.service;

import com.zosh.job.client.GeminiClient;
import com.zosh.job.payload.AiTextResponse;
import com.zosh.job.payload.JobDescriptionRequest;
import com.zosh.job.payload.SalaryRangeRequest;
import com.zosh.job.payload.SalaryRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobAiService {

    private final GeminiClient geminiClient;

    String SYSTEM_PROMPT = """
            You are a senior HR professional and technical recruiter with deep knowledge of Indian job market and hiring trends. 
            You specialize in writing job descriptions, compensation benchmarking, and talent acquisition.
            Always write in a professional, engaging, and inclusive tone. Avoid generic statements and ensure the content is tailored to the provided job details.
            When asked for JSON, respond ONLY with valid JSON - no explanation, no markdown fences.
            """;

    public AiTextResponse generateJobDescription(JobDescriptionRequest jobDescriptionRequest) throws Exception {

        String skills = jobDescriptionRequest.getSkills() != null ? String.join(", ", jobDescriptionRequest.getSkills()) : "Not specified";
        String prompt = """
                Write a comprehensicive, enaging, and inclusive job description.
                
                Job Details:
                - Title: %s
                - Required Skills: %s
                - Experience Level: %s
                - Job Type: %s
                - Work Mode: %s
                - Category: %s
                - Additional Context: %s
                
                Format the response in clean markdown with EXACTLY the following sections:
                ## About the Role
                [2-3 compelling sentences describing the role and its impact within the organization.]
                
                
                ## Key Responsibilities
                - [6 specific, action-oriented bullet points]
                
                ## Requirements
                - [5-6 must-have qualifications, skills]
                
                ## Nice to Have
                - [3-4 additional qualifications or skills that are desirable but not mandatory]
                
                ## What We Offer
                - [3-4 bullet points highlighting benefits, perks, and growth opportunities]
                
                DO NOT include placeholder company names or generic statements. Ensure the content is tailored to the provided job details and is engaging for potential candidates.
                
                """.formatted(
                jobDescriptionRequest.getTitle(),
                skills,
                jobDescriptionRequest.getExperienceLevel() != null ? jobDescriptionRequest.getExperienceLevel() : "Not specified",
                jobDescriptionRequest.getJobType() != null ? jobDescriptionRequest.getJobType() : "Not specified",
                jobDescriptionRequest.getWorkMode() != null ? jobDescriptionRequest.getWorkMode() : "Not specified",
                jobDescriptionRequest.getCategory() != null ? jobDescriptionRequest.getCategory() : "Not specified",
                jobDescriptionRequest.getAdditionalContext() != null ? jobDescriptionRequest.getAdditionalContext() : "Not specified"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public AiTextResponse generateJobRequirements(String title, String category) throws Exception {
        String prompt = """
                Write a comprehensive and engaging list of job requirements for the following role:
                
                Job Details:
                - Title: %s
                - Category: %s
                
                Format the response in clean markdown with EXACTLY the following sections:
                ## Responsibilities
                - [6 specific, action-oriented bullet points]
                
                ## Requirements
                - [5-6 must-have qualifications, skills]
                
                ## Nice to Have
                - [3-4 additional qualifications or skills that are desirable but not mandatory]
                
                Keep it concise, clear and ATS-friendly.  Avoid generic statements and ensure the content is tailored to the provided job details. 
                """.formatted(
                title,
                category != null ? category : "Not specified"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public SalaryRangeResponse suggestSalaryRange(SalaryRangeRequest salaryRangeRequest) throws Exception {

        String prompt = """
                Provide a realistic and competitive salary range for the following job role in India, considering the current market trends and industry standards.
                
                Role Details:
                - Job Title: %s
                - Required Skills: %s
                - Experience Level: %s
                - Job Type: %s
                - Location: %s
                
                {
                    "minSalary": "600000",
                    "maxSalary": "1200000",
                    "currency": "INR",
                    "period": "YEARLY",
                    "marketInsights": "Brief 1-2 sentence explanation of the salary range based on current market trends and industry standards."
                }
                
                minSalary and maxSalary must be numbers (not strings). Use realistic current Indian salary data for the specified role, skills, experience level, job type, and location. Provide a brief market insight explaining the salary range.
                """
                .formatted(
                        salaryRangeRequest.getTitle(),
                        salaryRangeRequest.getSkills() != null ? String.join(", ", salaryRangeRequest.getSkills()) : "Not specified",
                        salaryRangeRequest.getExperienceLevel() != null ? salaryRangeRequest.getExperienceLevel() : "Not specified",
                        salaryRangeRequest.getJobType() != null ? salaryRangeRequest.getJobType() : "Not specified",
                        salaryRangeRequest.getLocation() != null ? salaryRangeRequest.getLocation() : "Not specified"
                );

        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, SalaryRangeResponse.class);
    }

    public AiTextResponse generateJobResponsibilities(String title, String category) throws Exception{

        String prompt = """
                Write a comprehensive and engaging list of job responsibilities for the following role:
                
                Job Details:
                - Title: %s
                - Category: %s
                
                Format the response in clean markdown with EXACTLY the following sections:
                ## Responsibilities
                - [6 specific, action-oriented bullet points]
                
                Keep it concise, clear and ATS-friendly.  Avoid generic statements and ensure the content is tailored to the provided job details. 
                """.formatted(
                title,
                category != null ? category : "Not specified"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public AiTextResponse generateJobBenefits(String title, String category, String jobType) throws Exception{

        String prompt = """
                Write a comprehensive and engaging list of job benefits for the following role:
                
                Job Details:
                - Title: %s
                - Category: %s
                - Job Type: %s
                
                Format the response in clean markdown with EXACTLY the following sections:
                ## What We Offer
                - [3-4 bullet points highlighting benefits, perks, and growth opportunities]
                
                Include a mix of: compensation perks, health/wellness, growth opportunities, flexibility, work-life balance, and any unique benefits specific to the role or company.
                Keep it concise, clear and ATS-friendly.  Avoid generic statements and ensure the content is tailored to the provided job details. 
                """.formatted(
                title,
                category != null ? category : "General",
                jobType != null ? jobType : "Full Time"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public AiTextResponse recommendSkillsForJobs(String jobTitle, String description) throws Exception {

        String prompt = """
                Write a list of required skills for the following job:
                
                Job Details:
                - Title: %s
                - Description: %s
                
                List 8-10 specific, relevant skills that candidates should possess for this role.
                Return a comma-separated list of skills in plain text format, without any additional formatting or explanations.
                Example: Java, Spring Boot, RESTful APIs, SQL, Git, Agile Methodologies, Problem-Solving, Communication Skills
                """.formatted(
                jobTitle,
                description != null ? description : "Not specified"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public AiTextResponse recommendTagsForJobs(String jobTitle, String description) throws Exception {

        String prompt = """
                Recommend 5-7 relevant tags/keywords for the following job posting that improve its visibility and searchability on job boards and platforms:
                
                Job Details:
                - Title: %s
                - Description: %s
                
                Return ONLY comma-separated list of tags in plain text format, without any additional formatting or explanations.
                Example: Java, Spring Boot, RESTful APIs, SQL, Git, Agile Methodologies, Problem-Solving, Communication Skills
                """.formatted(
                jobTitle,
                description != null ? description : "Not specified"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

}