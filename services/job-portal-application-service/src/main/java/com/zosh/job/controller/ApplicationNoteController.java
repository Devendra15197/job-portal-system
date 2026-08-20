package com.zosh.job.controller;

import com.zosh.job.dto.ApiResponse;
import com.zosh.job.dto.ApplicationNoteResponse;
import com.zosh.job.payload.AddApplicationNoteRequest;
import com.zosh.job.service.ApplicationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/application-notes")
public class ApplicationNoteController {

    private final ApplicationNoteService applicationNoteService;

    @PostMapping
    public ResponseEntity<ApplicationNoteResponse> addNote(
            @PathVariable Long applicationId,
            @RequestHeader("X-User-Id") Long employerId,
            @RequestBody @Valid AddApplicationNoteRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationNoteService.addNote(applicationId, employerId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationNoteResponse>> getNotesByApplication(
            @PathVariable Long applicationId,
            @RequestHeader("X-User-Id") Long employerId) {
        return ResponseEntity.ok(applicationNoteService.getNoteByApplication(applicationId, employerId));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse> deleteNote(
            @PathVariable Long applicationId,
            @PathVariable Long noteId,
            @RequestHeader("X-User-Id") Long employerId) throws Exception {
        applicationNoteService.deleteNote(applicationId, noteId, employerId);
        return ResponseEntity.ok(new ApiResponse("Note Deleted Successfully", true));
    }
}