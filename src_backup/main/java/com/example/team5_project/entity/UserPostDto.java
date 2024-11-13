package com.example.team5_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPostDto {
    private String name;
    private String password;
    private String email;
    private Integer attendance;

    @Builder
    public UserPostDto(String name, String password, String email, Integer attendance) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.attendance = attendance;
    }
}
