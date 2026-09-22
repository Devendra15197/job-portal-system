package com.zosh.job.service;

import com.zosh.job.client.GeminiClient;
import com.zosh.job.payload.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeAiService {

    private final GeminiClient geminiClient;

    String SYSTEM_PROMPT = """
            You are a senior resume writer and career coach with 15+ years of experience in Indian Tech Job market.
            You specialize in ATS-Optimized resumes, career coaching, and professional branding.
            Always be specific and results-oriented. Never use generic phrases like "hard-working", "team-player", or "passionate".
            When asked for JSON, response ONLY with valid JSON - no explanation, no markdown fences.
            """;

    public AiTextResponse generateProfessionalSummary(ResumeSummaryRequest request) throws Exception {
        String experiences = request.getWorkExperiences() != null ?
                request.getWorkExperiences().stream()
                        .map(e -> e.getJobTitle() + " at " + e.getCompanyName() +
                                (e.getDescription() != null && !e.getDescription().isBlank() ? " (" + e.getDescription() + ")" : ""))
                        .collect(Collectors.joining("; ")) : "Not Provided";


        String skills = request.getTargetJobTitle() != null ? String.join(", ", request.getSkills()) : "Not Provided";

        String educations = request.getEducations() != null ?
                request.getEducations().stream()
                        .map(e -> e.getDegree()
                                + (e.getFieldOfStudy() != null ? " in " + e.getFieldOfStudy() : "")
                                + (e.getInstitutionName() != null ? " from " + e.getInstitutionName() : ""))
                        .collect(Collectors.joining("; ")) : "Not Provided";

        String prompt = """
                Write a compelling professional summary for a resume:
                
                Candidate Profile:
                - Target Job Title: %s
                - Years of Experience: %d
                - Work Experiences: %s
                - Key Skills: %s
                - Education: %s
                
                Write a 3-4 sentence professional summary that:
                1. Opens with seniority level and area of expertise.
                2. Highlights 2-3 key achievements or strengths with impact.
                3. Mentions specific technical skills relevant to target role.
                4. Ends with a value proposition or career goal aligned with the target job.
                
                Rules:
                - Write in first person (no "I" at the start)
                - Be specific and results-oriented
                - Keep it under 80 words
                - Make it ATS-friendly
                """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "Software Engineer",
                request.getYearsOfExperience() != null ? request.getYearsOfExperience() : 0,
                experiences,
                skills,
                educations
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();
    }

    public WorkExperienceBulletsResponse generateWorkExperienceBullets(WorkExperienceBulletsRequest request) throws Exception {
        // Implementation for generating work experience bullets

        String prompt = """
                Transform this work experience into powerful, ATS-friendly bullet points for a resume:
                
                Role: %s at %s
                Description: %s
                Achievements Hint: %s
                
                Rules:
                - Start with strong action verbs(Developed, Implemented, Led, Optimized, etc.)
                - Include quantifiable metrics where possible (percentages, numbers, timeframes)
                - Highlight business impact and technical achievements
                - Keep each bullet under 20 words.
                - Avoid generic phrases like "responsible for" or "worked on".
                - Are ATS-friendly with relevant keywords for the role.
                
                {
                  "bullets": ["bullet point 1", "bullet point 2", "bullet point 3", "bullet point 4", "bullet point 5"]
                }
                
                """.formatted(
                request.getJobTitle(),
                request.getCompany() != null ? request.getCompany() : "N/A",
                request.getRawDescription(),
                request.getAchievementsHint() != null ? request.getAchievementsHint() : "N/A"
        );
        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, WorkExperienceBulletsResponse.class);
    }

    public CareerFeedbackResponse getCareerFeedback(CareerFeedbackRequest request) throws Exception {
        // Implementation for generating career feedback
        String prompt = """     
                      Analyze this resume and deliver an honest, actionable career feedback report.
                
                      Target Job Title (if provided): %s
                      Resume Content:
                      %s
                
                      Return ONLY valid JSON in this exact structure:
                      {
                        "profileStrength": 65,
                        "shortlistingIssues": ["Reason 1 why recruiters are skipping this profile", "Reason 2", "Reason 3"],
                        "improvements": [
                          {"area": "Skills | Summary | Experience | Education | Projects | General", "issue": "What specifically is weak or missing", "action": "Concrete step the candidate should take", "priority": "HIGH | MEDIUM | LOW"}
                        ],
                        "targetJobs": [
                          {"jobTitle": "Recommended Job Title", "reason": "Why this role suits the current profile", "skillMatch": "HIGH | MEDIUM | LOW"}
                        ],
                        "overallSummary": "2-3 sentences of honest, encouraging career advice"
                      }
                
                      Rules:
                      - profileStrength: integer 0-100 reflecting overall job market readiness
                      - shortlistingIssues: 3-5 candid reasons a recruiter would skip this resume
                      - improvements: 4-6 items ordered by priority descending
                      - targetJobs: 3-5 realistic job titles matching current skills and experience level
                      - Be specific - mention actual skills, tools, or sections by name
                """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "N/A",
                request.getResumeContent() != null ? request.getResumeContent() : "N/A"
        );
        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, CareerFeedbackResponse.class);
    }

    public ResumeImprovementResponse getResumeImprovementTips(ResumeImprovementRequest request) throws Exception {
        // Implementation for generating resume improvement tips
        String prompt = """
                Analyze this resume and provide specific, actionable improvement suggestions.
                
                Target Job Title: %s
                
                Resume Content:
                %s
                
                {
                  "overallScore": 72,
                  "improvements": [
                    {
                      "section": "Summary or Experience or Skills or Education or General",
                      "issue": "What is wrong or missing",
                      "strengths": ["what is already good about this resume"],
                      "suggestion": "Specific action to fix it",
                      "priority": "High or Medium or Low"
                    }
                  ],
                  "summary": "2-sentence overall assessment"
                }
                
                Provide 4-6 specific improvements. Score should be 0-100.
                """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "N/A",
                request.getResumeContent() != null ? request.getResumeContent() : "N/A"

                );
        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, ResumeImprovementResponse.class);
    }

}
