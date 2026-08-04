package com.zosh.job.payload;

import com.zosh.job.domain.ResumeTemplate;
import com.zosh.job.domain.ResumeVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateResumeRequest {
    @NotBlank(message = "Resume Title is required")
    private String title;

    private ResumeTemplate template;
    private ResumeVisibility visibility;
    private Boolean isDefault;

}
