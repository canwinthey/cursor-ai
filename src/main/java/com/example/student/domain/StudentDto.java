package com.example.student.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    
    private Long id;
    
    @NotBlank(message = "Name is required and cannot be blank")
    private String name;
    
    @NotBlank(message = "Email is required and cannot be blank")
    @Email(message = "Email must be a valid email address")
    private String email;
    
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    private Integer age;
}

