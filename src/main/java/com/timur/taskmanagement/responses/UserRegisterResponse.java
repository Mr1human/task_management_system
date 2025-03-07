package com.timur.taskmanagement.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {
    private String email;
    private String id;
    private String message;
    private List<String> roles;
}
