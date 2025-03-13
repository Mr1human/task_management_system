package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.TaskService;
import com.timur.taskmanagement.services.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@Tag(name = "Задачи пользователей", description = "Контроллер для управления задачами пользователей")
public class UserController {
    private final UserTaskService userTaskService;

    public UserController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @PatchMapping("/update/{task_id}")
    @Operation(summary = "Обновление задачи", description = "Позволяет пользователю обновить свою задачу")
    public ResponseEntity<TaskResponse> updateTask(@Parameter(description = "ID задачи", example = "1")
                                                   @PathVariable Long task_id,
                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления")
                                                   @RequestBody TaskUpdateUserDTO taskUpdateUserDTO) throws AccessDeniedException {
        TaskResponse taskResponse = userTaskService
                .updateTask(task_id, taskUpdateUserDTO);
        return ResponseEntity.ok(taskResponse);
    }
}
