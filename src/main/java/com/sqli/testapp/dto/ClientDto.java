package com.sqli.testapp.dto;

import com.sqli.testapp.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDto {
    private int id;
    @Size(max = 50, message = "Name is too long")
    @NotBlank(message = "Name is required")
    private String name;
    @Size(max = 150, message = "Invalid email")
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;
    @Size(max = 250, message = "Password is too long")
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message="Age is required")
    @Digits(fraction = 0, integer = 2, message = "Invalid age")
    @Min(value = 18, message = "Age must be at least 18 years old")
    @Max(value = 99, message = "Age must be less than or equal to 99 years old")
    private Integer age;
    @NotNull(message = "Role is required")
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public ClientDto(int id, String name, String email, String password, int age, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public ClientDto(String name, String email, String password, int age, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role;
    }


}

