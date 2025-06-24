package com.zidioproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recruiter_profile")
@Getter
@Setter
public class RecruiterProfile {
    @Id
    private Long userId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "designation")
    private String designation;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


}
