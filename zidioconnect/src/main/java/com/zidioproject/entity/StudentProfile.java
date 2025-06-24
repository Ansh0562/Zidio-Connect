package com.zidioproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student")
public class StudentProfile {
    @Id
    private Long userId;

    private String resumeUrl;
    private String education;
    private String skills;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
