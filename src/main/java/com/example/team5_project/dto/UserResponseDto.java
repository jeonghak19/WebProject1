package com.example.team5_project.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private Integer attendance;

    @Builder
    public UserResponseDto(Long userId, String name, String email, Integer attendance) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.attendance = attendance;
    }
}


