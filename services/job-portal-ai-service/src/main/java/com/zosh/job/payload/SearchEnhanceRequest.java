package com.zosh.job.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchEnhanceRequest {
    @NotBlank(message = "Query cannot be blank")
    private String query;
}
