package com.zosh.job;

import com.zosh.job.controller.NotificationController;
import com.zosh.job.event.ApplicationStatusChangedEvent;
import com.zosh.job.html.BuildHtmlResponseForEmail;
import com.zosh.job.services.EmailNotificationService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.zosh.job.domain.ApplicationStatus.INTERVIEW_SCHEDULED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobPortalNotificationServiceApplicationTests {

    private MockMvc mockMvc;
    private EmailNotificationService emailNotificationService;
    private JavaMailSender mailSender;
    private BuildHtmlResponseForEmail buildHtmlResponseForEmail;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        buildHtmlResponseForEmail = new BuildHtmlResponseForEmail();
        emailNotificationService = new EmailNotificationService(mailSender, buildHtmlResponseForEmail);
        ReflectionTestUtils.setField(emailNotificationService, "fromEmail", "noreply@jobportal.local");

        mockMvc = MockMvcBuilders.standaloneSetup(new NotificationController(emailNotificationService)).build();
    }

    @Test
    void sendStatusChangeEmailEndpoint_shouldReturnSuccessMessage() throws Exception {
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mockMvc.perform(get("/api/notifications/send-status-change-email"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully"));
    }

    @Test
    void sendStatusChangeEmail_withEvent_shouldSendMail() throws Exception {
        MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                .applicationId(101L)
                .candidateEmail("candidate@example.com")
                .candidateName("Jane Doe")
                .jobTitle("Senior Java Developer")
                .companyName("Acme Corp")
                .oldStatus(com.zosh.job.domain.ApplicationStatus.PENDING)
                .newStatus(INTERVIEW_SCHEDULED)
                .note("Your interview has been scheduled.")
                .changedAt(LocalDateTime.of(2026, 10, 24, 9, 30))
                .build();

        emailNotificationService.sendStatusChangeEmail(event);

        verify(mailSender).send(mimeMessage);
    }
}
