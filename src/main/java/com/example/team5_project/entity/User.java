package com.example.team5_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    // 이름은 한글, 영어, 숫자만 허용, 최대 10자 제한, 공백 불가
    @Column(unique = true)
    @NotEmpty(message = "Name cannot be empty")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "Name can only contain Korean, English letters, and numbers, and must be up to 10 characters.")
    private String name;

    // 이메일은 이메일 형식 검사
    @Email(message = "Please provide a valid email address")
    private String email;

    // 패스워드는 한글, 영어, 숫자, 특수기호 포함 가능, 4-20자 제한
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9!@#\\$%\\^&\\*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$",
            message = "Password can contain Korean, English letters, numbers, and special characters")
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
