package com.example.team5_project.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPostDto {

    @NotEmpty(message = "Name cannot be empty")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "Name can only contain Korean, English letters, and numbers, and must be up to 10 characters.")
    private String name;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#\\$%\\^&\\*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$",
            message = "Password can contain Korean, English letters, numbers, and special characters")
    private String password;

    @Email(message = "Please provide a valid email address")
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


