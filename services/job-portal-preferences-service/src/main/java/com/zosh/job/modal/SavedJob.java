package com.zosh.job.modal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_jobs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private Long jobId;


    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime savedAt;

}
