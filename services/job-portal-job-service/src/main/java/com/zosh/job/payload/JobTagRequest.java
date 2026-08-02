package com.zosh.job.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTagRequest {

    @NotBlank(message = "Tag name is required")
    private String name;
}
