package com.zosh.job.modal;


import com.zosh.job.domain.LanguageProficiency;
import com.zosh.job.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private String languageName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageProficiency languageProficiency;

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
