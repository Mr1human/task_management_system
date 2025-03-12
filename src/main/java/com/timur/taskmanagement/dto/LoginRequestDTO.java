package com.timur.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequestDTO {
    @Email(message = "incorrect email")
    private String email;
    @Size (message = "incorrect password")
    private String password;
}
