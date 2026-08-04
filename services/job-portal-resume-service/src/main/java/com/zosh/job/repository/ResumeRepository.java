package com.zosh.job.repository;

import com.zosh.job.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByCandidateId(Long candidateId);

    Optional<Resume> findByCandidateIdAndIsDefaultTrue(Long resumeId);

}
