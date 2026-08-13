package com.zosh.job.modal;

import com.zosh.job.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resume resume;

    private String description;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> technologies;

    private String projectUrl;

    private String sourceCodeUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isOngoing = false;

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
