package com.zosh.job.service;

import com.zosh.job.client.GeminiClient;
import com.zosh.job.payload.AiTextResponse;
import com.zosh.job.payload.CoverLetterRequest;
import com.zosh.job.payload.ScreeningScoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationAiService {

    private final GeminiClient geminiClient;

    private static final String SYSTEM_PROMPT =
            """
                    You are a senior technical recruiter and career coach with 15+ years of experience in the Indian tech industry.
                    You specialize in candidate evaluation, cover letter writing, skills gap analysis, and career development.
                    Always provide objective, fair and actionable assessments based only on the information provided.
                    When asked for JSON, respond ONLY with valid JSON - no explanation, no markdown fences.
                    """;

    public AiTextResponse generateCoverLetter(CoverLetterRequest request) throws Exception {

        String skills = request.getCandidateSkills() != null ? String.join(", ", request.getCandidateSkills()) : "Not provided";
        String experience = request.getCandidateExperience() != null ? String.join(", ", request.getCandidateExperience()) : "Not provided";

        String userPrompt = String.format(
                """
                        Write a compelling, personalized cover letter.
                        Please consider the following details:
                        Position: %s
                        Job Description: %s
                        Target Company: %s
                        
                        Candidate Profile:
                        - Name: %s
                        - Professional Summary: %s
                        - Key Skills: %s
                        - Relevant Experience: %s
                        
                        Write a 3-paragraph cover letter:
                        Paragraph 1 (Opening): Express specific enthusiasm for this exact role and company. Mention 1 specific thing about the role that excites you. Keep it concise and engaging.
                        Paragraph 2 (Body): Connect 2-3 of the candidate's strongest experiences/skills directly to the job requirements. Be specific about how these experiences/skills make the candidate a strong fit for the role. Use clear examples and avoid generic statements.
                        Paragraph 3 (Closing): Confident call to action. Express eagerness to contribute and request an interview. Keep it professional and polite.
                        
                        Rules:
                        - Write as the candidate (first person perspective).
                        - Be specific - avoid generic statements.
                        - Maximum 300 words.
                        - Professional but warm tone. Avoid overly casual language.
                        - Do NOT use placeholders like [Company Name] - use the actual company name or say "your team".
                        - Do NOT include subject line or date.
                        """.formatted(
                        request.getJobTitle(),
                        request.getJobDescription() != null ? request.getJobDescription() : "Not provided",
                        request.getTargetCompanyName() != null ? request.getTargetCompanyName() : "Not provided",
                        request.getCandidateName(),
                        request.getCandidateSummary() != null ? request.getCandidateSummary() : "Not provided",
                        skills,
                        experience

                )
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, userPrompt))
                .build();

    }

    public ScreeningScoreResponse scoreCandidate(ScreeningScoreRequest request) throws Exception {
        String requiredSkills = request.getRequiredSkills() != null ? String.join(", ", request.getRequiredSkills()) : "Not provided";
        String candidateSkills = request.getCandidateSkills() != null ? String.join(", ", request.getCandidateSkills()) : "Not provided";
        String candidateExperience = request.getCandidateExperience() != null ? String.join(", ", request.getCandidateExperience()) : "Not provided";

        String userPrompt = String.format(
                """
                           Score this job application based on how well he candidate matches the requirements.
                        
                           Job Description:
                           - Job Title: %s
                           - Experience Level: %s
                           - Required Skills: %s
                           - Responsibilities: %s
                        
                           Candidate Profile:
                           - Summary: %s
                           - Skills: %s
                           - Experience: %s
                        
                           {
                               "score"L 85,
                               "skillsMatchScore": 90,
                               "experienceMatchScore": 80,
                               "educationMatchScore": 75,
                               "matchedSkills": ["Java", "Spring Boot"],
                               "missingSkills": ["AWS", "Docker"],
                               "strengths": ["Strong Java experience", "Good problem-solving skills"],
                               "concerns": ["Limited cloud experience", "No leadership experience"],
                               "summary": "2-3 sentence honest assessment of this candidate's fit for this role, highlighting strengths and areas for improvement."
                           }
                        
                           score scale: 0-100, where 100 is a perfect match and 0 is a complete mismatch.
                           skillsMatchScore: 0-100, where 100 means the candidate has all the required skills and 0 means none of the required skills.
                           experienceMatchScore: 0-100, where 100 means the candidate has all the required experience and 0 means none of the required experience.
                           educationMatchScore: 0-100, where 100 means the candidate has all the required education and 0 means none of the required education.
                           score: overall weighted score based on skills, experience, and education match scores. Be objective and fair in your assessment.
                        """.formatted(
                        request.getJobTitle() != null ? request.getJobTitle() : "Not provided",
                        request.getExperienceLevel() != null ? request.getExperienceLevel() : "Not provided",
                        requiredSkills,
                        request.getResponsibilities() != null ? request.getResponsibilities() : "Not provided",
                        request.getCandidateSummary() != null ? request.getCandidateSummary() : "Not provided",
                        candidateSkills,
                        candidateExperience
                )
        );

        return geminiClient.generateJson(SYSTEM_PROMPT, userPrompt, ScreeningScoreResponse.class);


    }
}
