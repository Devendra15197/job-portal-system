package com.zosh.job.service;

import com.zosh.job.dto.LanguageReponse;
import com.zosh.job.payload.AddLanguageRequest;

import java.util.List;

public interface LanguageService {
    LanguageReponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest languageRequest) throws Exception;

    List<LanguageReponse> getLanguages(Long resumeId);

    LanguageReponse updateLanguage(Long languageId, Long resumeId, Long candidateId, AddLanguageRequest languageRequest) throws Exception;

    void deleteLanguage(Long languageId, Long resumeId, Long candidateId) throws Exception;
}
