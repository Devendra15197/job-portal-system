package com.zosh.job.service.impl;

import com.zosh.job.dto.ProjectResponse;
import com.zosh.job.entity.Resume;
import com.zosh.job.mapper.ResumeMapper;
import com.zosh.job.modal.Project;
import com.zosh.job.payload.AddProjectRequest;
import com.zosh.job.repository.ProjectRepository;
import com.zosh.job.service.ProjectService;
import com.zosh.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    final ProjectRepository projectRepository;
    final ResumeService resumeService;

    @Override
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest addProjectRequest) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Project project = Project
                .builder()
                .resume(resume)
                .title(addProjectRequest.getTitle())
                .description(addProjectRequest.getDescription())
                .technologies(addProjectRequest.getTechnologies() != null ? addProjectRequest.getTechnologies() : List.of())
                .projectUrl(addProjectRequest.getProjectUrl())
                .sourceCodeUrl(addProjectRequest.getSourceCodeUrl())
                .startDate(addProjectRequest.getStartDate())
                .endDate(addProjectRequest.getEndDate())
                .isOngoing(Boolean.TRUE.equals(addProjectRequest.getIsOngoing()))
                .displayOrder(addProjectRequest.getDisplayOrder() != null ? addProjectRequest.getDisplayOrder() : 0)
                .build();

        Project saved = projectRepository.save(project);
        return ResumeMapper.toProjectResponse(saved);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Long resumeId) {
        return projectRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId).stream()
                .map(ResumeMapper::toProjectResponse)
                .toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, Long resumeId, Long candidateId, AddProjectRequest addProjectRequest) throws Exception {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found with Id " + projectId));
        assertOwner(project.getResume(), candidateId);


        project.setTitle(addProjectRequest.getTitle());
        project.setDescription(project.getDescription());
        if (addProjectRequest.getTechnologies() != null) project.setTechnologies(addProjectRequest.getTechnologies());
        project.setProjectUrl(project.getProjectUrl());
        project.setSourceCodeUrl(project.getSourceCodeUrl());
        project.setStartDate(project.getStartDate());
        project.setEndDate(project.getEndDate());
        project.setIsOngoing(project.getIsOngoing());
        if (project.getDescription() != null) project.setDisplayOrder(project.getDisplayOrder());

        return ResumeMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long projectId, Long resumeId, Long candidateId) throws Exception {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("Project not found with Id " + projectId));
        assertOwner(project.getResume(), candidateId);
        projectRepository.delete(project);
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception("Resume not found with Id ");
        }
    }
}
