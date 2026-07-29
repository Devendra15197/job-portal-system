package com.zosh.job.controller;

import com.zosh.job.domain.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Job Portal JOB Service!---" + UserRole.ROLE_ADMIN.name();
    }
}
