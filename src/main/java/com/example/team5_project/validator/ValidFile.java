package com.example.team5_project.validator;


import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = FileValidator.class)
@Target(value= ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidFile {
    String message() default "유효하지 않은 파일입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long maxSize() default 5 * 1024 * 1024;  // 기본 최대 파일 크기: 5MB
    String[] allowedTypes() default { "image/png", "image/jpeg" }; // 허용 파일 형식
}
