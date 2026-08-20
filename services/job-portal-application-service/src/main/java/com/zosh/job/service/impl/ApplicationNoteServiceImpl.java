package com.zosh.job.service.impl;

import com.zosh.job.dto.ApplicationNoteResponse;
import com.zosh.job.mapper.ApplicationMapper;
import com.zosh.job.modal.Application;
import com.zosh.job.modal.ApplicationNote;
import com.zosh.job.payload.AddApplicationNoteRequest;
import com.zosh.job.repository.ApplicationNoteRepository;
import com.zosh.job.service.ApplicationNoteService;
import com.zosh.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationNoteServiceImpl implements ApplicationNoteService {

    private final ApplicationNoteRepository repository;
    private final ApplicationService applicationService;

    @Override
    public ApplicationNoteResponse addNote(Long applicationId, Long employerId, AddApplicationNoteRequest req) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);
        ApplicationNote note = ApplicationNote.builder()
                .application(application)
                .addedByUserId(employerId)
                .content(req.getContent())
                .build();

        ApplicationNote savedNote = repository.save(note);

        return ApplicationMapper.toNoteResponse(savedNote);
    }

    @Override
    public List<ApplicationNoteResponse> getNoteByApplication(Long applicationId, Long employerId) {
        return repository.findByApplicationId(applicationId)
                .stream().map(ApplicationMapper::toNoteResponse).toList();
    }

    @Override
    public void deleteNote(Long applicationId, Long noteId, Long employerId) throws Exception {
        Application application = applicationService.getApplicationEntity(applicationId);
        assertEmployer(application, employerId);

        ApplicationNote note = repository.findById(noteId)
                .orElseThrow(() -> new Exception("Note not found"));
        repository.delete(note);
    }

    private void assertEmployer(Application application, Long employerId) throws Exception {
        if (!application.getEmployerId().equals(employerId)) {
            throw new Exception("You are not authorized to update this application");
        }
    }
}
