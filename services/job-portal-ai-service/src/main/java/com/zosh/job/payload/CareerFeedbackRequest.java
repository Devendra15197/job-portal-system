package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CareerFeedbackRequest {

    @NotBlank(message = "Resume content cannot be blank")
    private String resumeContent;

    private String targetJobTitle;
}
