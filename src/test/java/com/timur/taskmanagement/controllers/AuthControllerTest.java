package com.timur.taskmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerSuccessTest() throws Exception {
        String email = "testuser" + System.currentTimeMillis() + "@mail.com";
        registerUser(email, "123456");
    }

    @Test
    void registerTestInvalidEmailFormat() throws Exception {
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\", \"password\":\"123456\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("incorrect email!"));
    }


    @Test
    void loginSuccessTest() throws Exception {
        String email = "testuser" + System.currentTimeMillis() + "@mail.com";
        String password = "123456";
        registerUser(email, "123456");

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(email);
        loginRequestDTO.setPassword(password);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtAccessToken").exists())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void loginInvalidPasswordTest() throws Exception {
        String email = "testuser" + System.currentTimeMillis() + "@mail.com";
        String password = "123456";
        registerUser(email, password);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(email);
        loginRequestDTO.setPassword("123");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").value("Invalid email or password"));
    }

    void registerUser(String email, String password) throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail(email);
        registerRequestDTO.setPassword(password);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User is registered!"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.roles").value(contains("ROLE_USER")));
    }
}
