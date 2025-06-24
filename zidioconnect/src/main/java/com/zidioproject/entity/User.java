package com.zidioproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Getter
    @Column(unique = true)
    private String email;

    private String password;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    private String username;

}
