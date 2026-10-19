package com.zosh.job.controller;

import com.zosh.job.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final EmailNotificationService emailNotificationService;

    @GetMapping("/send-status-change-email")
    public String sendStatusChangeEmail() throws Exception {
        emailNotificationService.sendStatusChangeEmail();
        return "Email sent successfully";
    }
}
