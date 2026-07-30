package com.zosh.job.repository;

import com.zosh.job.modal.Job;
import com.zosh.job.payload.JobSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {
    private JobSpecification(){

    }

    public static Specification<Job> build(JobSearchRequest req){

    }
}
