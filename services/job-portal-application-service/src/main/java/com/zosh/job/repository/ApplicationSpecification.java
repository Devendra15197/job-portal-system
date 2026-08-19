package com.zosh.job.repository;

import com.zosh.job.domain.AiShortListStatus;
import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.modal.Application;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSpecification {

    public static Specification<Application> forCompanyWithFilters(
            Long companyId,
            Long jobId,
            ApplicationStatus status,
            boolean isStarred,
            AiShortListStatus aiShortListStatus,
            Integer minAiScore
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("companyId"), companyId));
            predicates.add(criteriaBuilder.equal(root.get("jobId"), jobId));
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
            predicates.add(criteriaBuilder.equal(root.get("isStarred"), isStarred));
            if (aiShortListStatus != null) predicates.add(criteriaBuilder.equal(root.get("aiShortListStatus"), aiShortListStatus));
            if (minAiScore != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("aiScore"), minAiScore));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
