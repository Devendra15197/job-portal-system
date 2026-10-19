package com.zosh.job.event;

import com.zosh.job.services.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {
    private final EmailNotificationService emailNotificationService;

    @KafkaListener(
            topics = "application.status.change",
            groupId = "job-portal-notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleStatusChangeEvent(ApplicationStatusChangedEvent event) throws Exception {
        System.out.println("Received application status change event: " + event);
        emailNotificationService.sendStatusChangeEmail(event);
    }
}
