package com.zosh.job.payload;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WithdrawApplicationRequest {
    private String reason;
}
