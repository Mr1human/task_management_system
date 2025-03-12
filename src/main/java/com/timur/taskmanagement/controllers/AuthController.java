package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.RegisterRequestDTO;
import com.timur.taskmanagement.responses.JwtResponse;
import com.timur.taskmanagement.responses.UserRegisterResponse;
import com.timur.taskmanagement.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegisterResponse> registration(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserRegisterResponse userRegisterResponse = authService.register(registerRequest);
        return ResponseEntity.ok(userRegisterResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        JwtResponse jwtResponse = authService.login(loginRequestDTO);
        return ResponseEntity.ok(jwtResponse);

    }
}
