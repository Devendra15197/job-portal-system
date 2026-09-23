package com.zosh.job.payload;

import lombok.Data;

@Data
public class SalaryRangeResponse {
    private String minSalary;
    private String maxSalary;
    private String currency;
    private String period;
    private String marketInsights;
}
