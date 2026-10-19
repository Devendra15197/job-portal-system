package com.zosh.job.event;

import com.zosh.job.client.CompanyClient;
import com.zosh.job.client.JobClient;
import com.zosh.job.client.UserClient;
import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.dto.JobResponse;
import com.zosh.job.dto.response.CompanyResponse;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.modal.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventPublisher {

    public static final String TOPIC = "application.status.change";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserClient userClient;
    private final CompanyClient companyClient;
    private final JobClient jobClient;

    public void publishApplicationStatusChangeEvent(Application app, ApplicationStatus oldStatus, ApplicationStatus newStatus, String note) {
        try{
            UserResponse user = userClient.getUserById(app.getCandidateId());
            JobResponse job = jobClient.getJobById(app.getJobId());
            CompanyResponse company = companyClient.getCompanyById(app.getCompanyId());

            ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                    .applicationId(app.getId())
                    .candidateId(app.getCandidateId())
                    .candidateEmail(user.getEmail())
                    .candidateName(user.getFullName())
                    .oldStatus(oldStatus)
                    .newStatus(newStatus)
                    .note(note)
                    .jobTitle(job.getTitle())
                    .companyName(company.getName())
                    .changedAt(app.getUpdatedAt())
                    .build();

            kafkaTemplate.send(TOPIC, String.valueOf(app.getId()), event);
        }catch (Exception e){
            // Log the error
            System.err.println("Error publishing application status change event: " + e.getMessage());
        }
    }
}
