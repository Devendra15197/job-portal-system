package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddApplicationNoteRequest {
    @NotBlank(message = "Note Content cannot be blank")
    @Size(min = 1, max = 2000, message = "Note Content must be between 1 and 2000 characters")
    private String content;
}
