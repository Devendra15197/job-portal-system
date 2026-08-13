package com.zosh.job.repository;

import com.zosh.job.modal.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
