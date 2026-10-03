package com.zosh.job.controller;

import com.zosh.job.payload.JobAlertSuggestRequest;
import com.zosh.job.payload.JobAlertSuggestResponse;
import com.zosh.job.payload.SearchEnhanceRequest;
import com.zosh.job.payload.SearchEnhanceResponse;
import com.zosh.job.service.SearchAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiSerchController {
    private final SearchAiService searchAiService;

    @PostMapping("/enhance")
    public ResponseEntity<SearchEnhanceResponse> enhanceSearch(@Valid @RequestBody SearchEnhanceRequest request) throws Exception {
        SearchEnhanceResponse response = searchAiService.enhanceSearchRequest(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alert-suggestion")
    public ResponseEntity<JobAlertSuggestResponse> suggestJobAlert(@Valid @RequestBody JobAlertSuggestRequest request) throws Exception {
        JobAlertSuggestResponse response = searchAiService.suggestJobAlerts(request);
        return ResponseEntity.ok(response);
    }

}
