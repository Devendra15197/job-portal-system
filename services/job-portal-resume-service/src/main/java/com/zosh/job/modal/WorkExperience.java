package com.zosh.job.modal;

import com.zosh.job.domain.JobType;
import com.zosh.job.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "work_experience")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private String companyName;

    private String companyLogoUrl;

    @Column(nullable = false)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private JobType employmentType;

    private String location;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isCurrentJob = false;

    private String description;

    @ElementCollection
    private List<String> technologies = new ArrayList<>();

    @Column(nullable = false)
    private Integer displayOrder = 0;
}
