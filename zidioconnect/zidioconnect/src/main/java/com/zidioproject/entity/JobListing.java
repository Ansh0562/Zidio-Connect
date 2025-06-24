package com.zidioproject.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "job_listing")
@NoArgsConstructor
@AllArgsConstructor
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String type; // JOB or INTERNSHIP

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Boolean remote;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private Integer durationWeeks;

    @Column(nullable = false)
    private Double stipend;

    @Column(nullable = false)
    private LocalDateTime postedAt;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        postedAt = LocalDateTime.now();
    }
}

