package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.RegisterRequestDTO;
import com.timur.taskmanagement.responses.JwtResponse;
import com.timur.taskmanagement.responses.UserRegisterResponse;
import com.timur.taskmanagement.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Аутентификация",
        description = "Контроллер, предназначенный для аутентификации (регистарция, логин)")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация пользователя", description = "Создаёт нового пользователя в системе")
    @PostMapping("/registration")
    public ResponseEntity<UserRegisterResponse> registration(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для регистрации пользователя")
            @Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserRegisterResponse userRegisterResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegisterResponse);
    }

    @Operation(summary = "Аутентификация пользователя", description = "Позволяет пользователю войти в систему")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для входа в систему")
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        JwtResponse jwtResponse = authService.login(loginRequestDTO);
        return ResponseEntity.ok(jwtResponse);
    }
}
