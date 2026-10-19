package com.zosh.job.services;

import com.zosh.job.event.ApplicationStatusChangedEvent;
import com.zosh.job.html.BuildHtmlResponseForEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    private final BuildHtmlResponseForEmail buildHtmlResponseForEmail;
    @Value("${spring.mail.username}")
    private String fromEmail;

    /// This is a test method to send email without any event, you can remove it later
    public void sendStatusChangeEmail() throws Exception {
        try {
            String subject = "Job Application Status Update";
            String body = buildHtmlResponseForEmail.buildStatusChangeEmailHtml();
            String candidateEmail = "devendrapawar666@gmail.com"; // Replace with the candidate's email address
            sendEmail(candidateEmail, subject, body);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void sendStatusChangeEmail(ApplicationStatusChangedEvent event) throws Exception {
        try {
            String subject = "Job Application Status Update" + event.getJobTitle() + " at " + event.getCompanyName();
            String body = buildHtmlResponseForEmail.buildStatusChangeEmailHtml(event);
            String candidateEmail = event.getCandidateEmail();
            sendEmail(candidateEmail, subject, body);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void sendEmail(String candidateEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(candidateEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);
        mailSender.send(mimeMessage);
    }


}
