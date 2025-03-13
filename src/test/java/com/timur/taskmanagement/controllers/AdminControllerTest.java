package com.timur.taskmanagement.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
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
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminJwtAccessToken;

    @BeforeEach
    void setup() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("admin@mail.ru");
        loginRequestDTO.setPassword("123");

        String response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@mail.ru\", \"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        adminJwtAccessToken = jsonNode.get("jwtAccessToken").asText();
        assertThat(adminJwtAccessToken).isNotEmpty();
    }

    @Test
    void createTaskSuccessAndGetTask() throws Exception {
        Long taskId = createTask("createTaskSuccessAndGetTask",
                "Test createTaskSuccessAndGetTask", 1L);

        mockMvc.perform(get("/admin/tasks/" + taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("createTaskSuccessAndGetTask"))
                .andExpect(jsonPath("$.description").value("Test createTaskSuccessAndGetTask"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    void updateTask_Success() throws Exception {
        Long taskId = createTask("updateTask_Success",
                "Test updateTask_Success", 1L);

        TaskUpdateAdminDTO taskUpdateAdminDTO = new TaskUpdateAdminDTO();
        taskUpdateAdminDTO.setPriority(TaskPriority.HIGH);
        taskUpdateAdminDTO.setStatus(TaskStatus.READY);

        mockMvc.perform(patch("/admin/update/" + taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateAdminDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updateTask_Success"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("READY"));
    }

    @Test
    void deleteTask_Success() throws Exception {
        Long taskId = createTask("deleteTask_Success",
                "Test deleteTask_Success", 1L);

        mockMvc.perform(delete("/admin/delete/" + taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("task has been deleted!"));
    }

    @Test
    void getTasks_Success() throws Exception {
        mockMvc.perform(get("/admin/tasks")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/admin/tasks/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
