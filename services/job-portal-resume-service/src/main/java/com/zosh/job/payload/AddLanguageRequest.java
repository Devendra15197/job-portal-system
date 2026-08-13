package com.zosh.job.payload;

import com.zosh.job.domain.LanguageProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddLanguageRequest {

    @NotBlank(message = "Language name is required")
    private String languageName;

    @NotNull(message = "Language proficiency is required")
    private LanguageProficiency languageProficiency;

    private Integer displayOrder;
}
