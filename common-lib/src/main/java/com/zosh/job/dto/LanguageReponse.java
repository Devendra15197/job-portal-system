package com.zosh.job.dto;

import com.zosh.job.domain.LanguageProficiency;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageReponse {
    private Long id;
    private String languageName;
    private LanguageProficiency languageProficiency;
    private Integer displayOrder;
}
