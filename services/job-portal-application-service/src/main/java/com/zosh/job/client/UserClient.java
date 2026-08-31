package com.zosh.job.client;

import com.zosh.job.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-portal-user-service")
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);
}
