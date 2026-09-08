package com.zosh.job.client;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.zosh.job.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final Client genaiClient;
    private final GeminiProperties geminiProperties;

    public String generateText(String systemInstruction, String prompt) throws Exception {
        return generateText(systemInstruction, prompt, geminiProperties.getTemperature(), geminiProperties.getMaxOutputTokens());
    }

    public String generateText(String systemInstruction, String prompt, double temperature, int maxTokens) throws Exception {
        return callText(systemInstruction, prompt, (float) temperature, maxTokens);
    }

    private String callText(String systemInstruction, String prompt, float temperature, int maxTokens) throws Exception {
        try {
            GenerateContentConfig config = buildConfig(systemInstruction, temperature, maxTokens, false);

            GenerateContentResponse response = genaiClient.models.generateContent(geminiProperties.getModel(), prompt, config);
            String text = response.text();
            return text;
        } catch (Exception e) {
            // preserve the original exception as the cause so we get the full stacktrace in logs
            throw new RuntimeException("Failed to get response from Gemini: " + e.getMessage(), e);
        }
    }

    private GenerateContentConfig buildConfig(String systemInstruction, float temperature, int maxTokens, boolean jsonMode) {
        GenerateContentConfig.Builder builder = GenerateContentConfig.builder()
                .temperature(temperature)
                .maxOutputTokens(maxTokens);

        if (systemInstruction != null && !systemInstruction.isEmpty()) {
            // Add system instruction to the config if provided
            builder.systemInstruction(Content.fromParts(Part.fromText(systemInstruction)));

        }
        if (jsonMode) {
            builder.responseMimeType("application/json");
        }
        return builder.build();
    }
}
