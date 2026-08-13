package com.zosh.job.service.impl;

import com.zosh.job.dto.LanguageReponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.Language;
import com.zosh.job.payload.AddLanguageRequest;
import com.zosh.job.repository.LanguageRepository;
import com.zosh.job.service.LanguageService;
import com.zosh.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final ResumeService resumeService;

    @Override
    public LanguageReponse addLanguage(Long resumeId, Long candidateId, AddLanguageRequest languageRequest) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Language language = Language
                .builder()
                .resume(resume)
                .languageName(languageRequest.getLanguageName())
                .languageProficiency(languageRequest.getLanguageProficiency())
                .displayOrder(languageRequest.getDisplayOrder() != null ? languageRequest.getDisplayOrder() : 0)
                .build();
        Language saved = languageRepository.save(language);
        return ResumeMapper.toLanguageResponse(saved);
    }

    @Override
    public List<LanguageReponse> getLanguages(Long resumeId) {
        return languageRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toLanguageResponse)
                .toList();
    }

    @Override
    public LanguageReponse updateLanguage(Long languageId, Long resumeId, Long candidateId, AddLanguageRequest languageRequest) throws Exception {
        Language language = languageRepository.findById(languageId).orElseThrow(() -> new Exception("Language not found with Id"));
        assertOwner(language.getResume(), candidateId);

        language.setLanguageName(languageRequest.getLanguageName());
        language.setLanguageProficiency(languageRequest.getLanguageProficiency());
        if (languageRequest.getDisplayOrder() != null) language.setDisplayOrder(languageRequest.getDisplayOrder());

        Language saved = languageRepository.save(language);
        return ResumeMapper.toLanguageResponse(saved);
    }

    @Override
    public void deleteLanguage(Long languageId, Long resumeId, Long candidateId) throws Exception {
        Language language = languageRepository.findById(languageId).orElseThrow(() -> new Exception("Language not found with Id"));
        assertOwner(language.getResume(), candidateId);

        languageRepository.delete(language);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}
