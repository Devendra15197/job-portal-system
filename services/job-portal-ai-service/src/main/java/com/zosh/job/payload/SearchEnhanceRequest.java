package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchEnhanceRequest {
    @NotBlank(message = "Query cannot be blank")
    private String query;
}
