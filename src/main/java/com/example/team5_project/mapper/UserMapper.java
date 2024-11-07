package com.example.team5_project.mapper;


import com.example.team5_project.dto.UserPostDto;
import com.example.team5_project.dto.UserResponseDto;
import com.example.team5_project.entity.User;


public class UserMapper {
    public static User toEntity(UserPostDto userPostDto) {
        return User.builder()
                .name(userPostDto.getName())
                .password(userPostDto.getPassword())
                .email(userPostDto.getEmail())
                .attendance(userPostDto.getAttendance())
                .build();
    }
    public static UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .attendance(user.getAttendance())
                .build();
    }
}

