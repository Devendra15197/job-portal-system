package com.zosh.job.repository;

import com.zosh.job.modal.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
    List<SavedJob> findByCandidateId(Long candidateId);
}
