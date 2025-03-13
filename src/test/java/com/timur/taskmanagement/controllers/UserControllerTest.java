package com.timur.taskmanagement.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timur.taskmanagement.dto.*;
import com.timur.taskmanagement.enums.TaskPriority;
import com.timur.taskmanagement.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminJwtAccessToken;
    private String userJwtAccessToken;
    private Long userId;

    @BeforeEach
    void setup() throws Exception {
        //вход админа

        String responseAdmin = responseLoginAdmin("admin@mail.ru", "123");
        JsonNode jsonNodeAdmin = objectMapper.readTree(responseAdmin);
        adminJwtAccessToken = jsonNodeAdmin.get("jwtAccessToken").asText();
        assertThat(adminJwtAccessToken).isNotEmpty();

        //регистрация и вход юзера
        String responseUser = responseLoginUser("usertest@mail.com", "123");
        JsonNode jsonNodeUser = objectMapper.readTree(responseUser);
        userJwtAccessToken = jsonNodeUser.get("jwtAccessToken").asText();
        assertThat(userJwtAccessToken).isNotEmpty();
        userId = jsonNodeUser.get("userId").asLong();
    }


    @Test
    void updateTaskTest() throws Exception {
        Long taskId = createTask("updateTaskTest", "Test updateTaskTest", userId);

        TaskUpdateUserDTO taskUpdateUserDTO = new TaskUpdateUserDTO();
        taskUpdateUserDTO.setStatus(TaskStatus.READY);

        //апдейт таски
        mockMvc.perform(patch("/update/{task_id}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respUserId").value(userId))
                .andExpect(jsonPath("$.title").value("updateTaskTest"))
                .andExpect(jsonPath("$.status").value("READY"));
    }

    public String responseLoginAdmin(String email, String password) throws Exception {
        LoginRequestDTO adminLoginRequestDTO = new LoginRequestDTO();
        adminLoginRequestDTO.setEmail(email);
        adminLoginRequestDTO.setPassword(password);

        String responseAdmin = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return responseAdmin;
    }

    public String responseLoginUser(String email, String password) throws Exception {
//регистрация и вход юзера
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail(email);
        registerRequestDTO.setPassword(password);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginRequestDTO userLoginRequestDTO = new LoginRequestDTO();
        userLoginRequestDTO.setEmail(email);
        userLoginRequestDTO.setPassword(password);

        String responseUser = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return responseUser;
    }

    private Long createTask(String title, String description, Long respUserId) throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle(title);
        taskCreateDTO.setDescription(description);
        taskCreateDTO.setStatus(TaskStatus.IN_PROGRESS);
        taskCreateDTO.setPriority(TaskPriority.LOW);
        taskCreateDTO.setRespUserId(respUserId);

        String createResponse = mockMvc.perform(post("/admin/create-task")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(createResponse);
        return jsonNode.get("id").asLong();
    }
}
