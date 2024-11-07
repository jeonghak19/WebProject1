package com.example.team5_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private String email;
    private String password;
    private Integer attendance;

    @Builder
    public User(String name, String email, String password, Integer attendance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.attendance = attendance;
    }
}

