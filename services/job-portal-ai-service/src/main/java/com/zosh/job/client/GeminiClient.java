package com.zosh.job.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.zosh.job.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final Client genaiClient;
    private final GeminiProperties geminiProperties;

    public String generateText(String prompt) {
        return generateText(prompt, geminiProperties.getTemperature(), geminiProperties.getMaxOutputTokens());
    }

    public String generateText(String prompt, double temperature, int maxTokens) {
        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .temperature((float) temperature)
                    .maxOutputTokens(maxTokens)
                    .build();

            GenerateContentResponse response = genaiClient.models.generateContent(geminiProperties.getModel(), prompt, config);
            String text = response.text();
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from Gemini: " + e.getMessage());
        }
    }
}
