package com.zosh.job.client;

import com.zosh.job.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-PORTAL-JOB-SERVICE")
public interface JobClient {
    @GetMapping("/api/jobs/{jobId}")
    JobResponse getJobById(@PathVariable Long jobId);
}

