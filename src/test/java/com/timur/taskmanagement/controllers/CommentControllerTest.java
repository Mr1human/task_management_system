package com.timur.taskmanagement.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timur.taskmanagement.dto.CommentDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminJwtAccessToken;
    private String userJwtAccessToken;
    private Long userId;

    @BeforeEach
    void setup() throws Exception {
        adminJwtAccessToken = authenticateAdmin();
        userJwtAccessToken = authenticateUser();
    }

    @Test
    void createTaskAndAddCommentAndGetCommentTest() throws Exception {
        Long taskId = createTask("createTaskAndAddCommentAndGetCommentTest",
                "test createTaskAndAddCommentAndGetCommentTest", userId);
        CommentDTO commentCreateDTO = createCommentDTO(taskId, "test comment");
        createComment(commentCreateDTO);
        getComments(taskId);
    }

    private String authenticateAdmin() throws Exception {
        LoginRequestDTO adminLoginRequestDTO = new LoginRequestDTO();
        adminLoginRequestDTO.setEmail("admin@mail.ru");
        adminLoginRequestDTO.setPassword("123");

        String responseAdmin = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNodeAdmin = objectMapper.readTree(responseAdmin);
        String jwtAccessToken = jsonNodeAdmin.get("jwtAccessToken").asText();
        assertThat(jwtAccessToken).isNotEmpty();
        return jwtAccessToken;
    }

    private String authenticateUser() throws Exception {
        String email = "testuser" + System.currentTimeMillis() + "@mail.com";
        String password = "123";

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail(email);
        registerRequestDTO.setPassword(password);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated());

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

        JsonNode jsonNodeUser = objectMapper.readTree(responseUser);
        userId = jsonNodeUser.get("userId").asLong();
        String jwtAccessToken = jsonNodeUser.get("jwtAccessToken").asText();
        assertThat(jwtAccessToken).isNotEmpty();
        return jwtAccessToken;
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

    private CommentDTO createCommentDTO(Long taskId, String text) {
        CommentDTO commentCreateDTO = new CommentDTO();
        commentCreateDTO.setTaskId(taskId);
        commentCreateDTO.setText(text);
        return commentCreateDTO;
    }

    private void createComment(CommentDTO commentCreateDTO) throws Exception {
        mockMvc.perform(post("/create-comment")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(commentCreateDTO.getTaskId()))
                .andExpect(jsonPath("$.text").value(commentCreateDTO.getText()));
    }

    private void getComments(Long taskId) throws Exception {
        mockMvc.perform(get("/comments/{task_id}", taskId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value(taskId))
                .andExpect(jsonPath("$[0].authorCommentId").value(userId));
    }
}
