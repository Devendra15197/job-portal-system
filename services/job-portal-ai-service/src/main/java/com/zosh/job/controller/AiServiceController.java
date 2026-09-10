package com.zosh.job.controller;

import com.zosh.job.client.GeminiClient;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiServiceController {
    private final GeminiClient geminiClient;


    @GetMapping("/{prompt}")
    public ResponseEntity<String> testAi(@PathVariable String prompt) throws Exception {

        //TODO: Add system instruction to the prompt to improve the quality of the response
        String systemInstruction = "You are a helpful assistant that provides concise and accurate responses.";
        String generatedText = geminiClient.generateText(systemInstruction, prompt);
        return ResponseEntity.ok(generatedText);
    }

}
