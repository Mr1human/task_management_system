package com.timur.taskmanagement.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.RegisterRequestDTO;
import com.timur.taskmanagement.dto.TaskCreateDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminJwtAccessToken;
    private String userJwtAccessToken;
    private Long userId;
    private Long adminId;

    @BeforeEach
    void setup() throws Exception {

        String responseAdmin = responseLoginAdmin("admin@mail.ru", "123");
        JsonNode jsonNodeAdmin = objectMapper.readTree(responseAdmin);
        adminJwtAccessToken = jsonNodeAdmin.get("jwtAccessToken").asText();
        adminId = jsonNodeAdmin.get("userId").asLong();
        assertThat(adminJwtAccessToken).isNotEmpty();


        String emailUser = "usertest" + System.currentTimeMillis() + "@mail.com";
        String responseUser = responseLoginUser(emailUser, "123");
        JsonNode jsonNodeUser = objectMapper.readTree(responseUser);
        userJwtAccessToken = jsonNodeUser.get("jwtAccessToken").asText();
        assertThat(userJwtAccessToken).isNotEmpty();
        userId = jsonNodeUser.get("userId").asLong();
    }


    @Test
    void getTaskByIdTest() throws Exception {
        Long taskId = createTask("getTaskByIdTest", "Test getTaskByIdTest", userId);

        //запрос от админа
        mockMvc.perform(get("/search-tasks/{task_id}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("getTaskByIdTest"));

        //от исполнителя
        mockMvc.perform(get("/search-tasks/{task_id}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("getTaskByIdTest"))
                .andExpect(jsonPath("$.respUserId").value(userId));

    }

    @Test
    void getTaskByIdNoAccessTest() throws Exception {
        Long taskId = createTask("getTaskByIdNoAccessTest", "Test getTaskByIdNoAccessTest", adminId);

        //от исполнителя
        mockMvc.perform(get("/search-tasks/{task_id}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    @Test
    void getTasksByRespUserTest() throws Exception {
        //создание таски
        Long taskId = createTask("getTasksByRespUserTest", "Test getTasksByRespUserTest", userId);

        //от исполнителя
        mockMvc.perform(get("/search-tasks/by-resp-user")
                        .param("respUserId", String.valueOf(userId))
                        .param("page", "0")
                        .param("size", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getTasksByAuthorIdTest() throws Exception {
        //создание таски
        Long taskId = createTask("getTasksByAuthorIdTest", "Test getTasksByAuthorIdTest", userId);

        //от исполнителя
        mockMvc.perform(get("/search-tasks/by-author")
                        .param("authorId", String.valueOf(adminId))
                        .param("page", "0")
                        .param("size", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getTasksByAuthorIdNoAccessTest() throws Exception {
        //создание таски
        Long taskId = createTask("getTasksByAuthorIdNoAccessTest", "Test getTasksByAuthorIdNoAccessTest", userId);

        //от исполнителя
        mockMvc.perform(get("/search-tasks/by-author")
                        .param("authorId", String.valueOf(adminId))
                        .param("page", "0")
                        .param("size", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTasksByRespUserIdNoAccessTest() throws Exception {
        createTask("getTasksByRespUserIdNoAccessTest", "Test getTasksByRespUserIdNoAccessTest",
                adminId);

        mockMvc.perform(get("/search-tasks/by-resp-user")
                        .param("respUserId", String.valueOf(adminId))
                        .param("page", "0")
                        .param("size", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
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
}
