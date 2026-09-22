package com.zosh.job.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerFeedbackResponse {
    private int profileStrength;
    private List<String> shortlistingIssues;
    private List<Improvement> improvements;
    private List<JobTarget> targetJobs;
    private String overallSummary;

    @Data
    public static class Improvement {
        private String area;
        private String issue;
        private String action;
        private String priority;
    }

    @Data
    public static class JobTarget {
        private String jobTitle;
        private String reason;
        private String skillMatch;
    }
}
