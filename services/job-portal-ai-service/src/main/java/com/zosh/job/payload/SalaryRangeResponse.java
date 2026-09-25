package com.zosh.job.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryRangeResponse {
    private String minSalary;
    private String maxSalary;
    private String currency;
    private String period;
    private String marketInsights;
}
