package com.zosh.job.repository;

import com.zosh.job.modal.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByResume_IdOrderByDisplayOrderAsc(Long resumeId);
}
