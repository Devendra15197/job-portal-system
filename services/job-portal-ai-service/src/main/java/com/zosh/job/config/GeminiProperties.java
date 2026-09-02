package com.zosh.job.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeminiProperties {
    private String key;
    private String model;
    private int maxOutputToken = 2048;
    private double temperature = 0.7;
}
