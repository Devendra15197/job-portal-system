package com.zosh.job.service.impl;

import com.zosh.job.dto.SavedJobResponse;
import com.zosh.job.mapper.PreferenceMapper;
import com.zosh.job.modal.SavedJob;
import com.zosh.job.payload.SaveJobRequest;
import com.zosh.job.repository.SavedJobRepository;
import com.zosh.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    public SavedJobResponse saveJob(Long candidateId, SaveJobRequest req) throws Exception {
        if (isSaved(candidateId, req.getJobId())) {
            throw new Exception("Job already saved");
        }
        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(req.getJobId())
                .build();

        savedJob = savedJobRepository.save(savedJob);

        return PreferenceMapper.toSavedJobResponse(savedJob);
    }

    @Override
    public void unSaveJob(Long candidateId, Long savedJobId) throws Exception {
        SavedJob savedJob = savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> new Exception("Saved job not found"));
        if (!savedJob.getCandidateId().equals(candidateId))
            throw new Exception("Job Not Saved");

        savedJobRepository.delete(savedJob);
    }

    @Override
    public List<SavedJobResponse> getSavedJob(Long candidateId) {
        return savedJobRepository.findByCandidateId(candidateId)
                .stream().map(PreferenceMapper::toSavedJobResponse).toList();
    }

    @Override
    public boolean isSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }
}
