package com.timur.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @Email(message = "incorrect email!")
    private String email;
    @Size(min = 3, message = "short password!")
    private String password;
}
