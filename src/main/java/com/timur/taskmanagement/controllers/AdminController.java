package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.responses.MessageResponse;
import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.AdminTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Панель администратора",
        description = "Контроллер, предназначаенный для администратора," +
                " позволяющий управлять задачами (создание задачи, редактирование, удаление, просмотр)")
public class AdminController {

    private final AdminTaskService adminTaskService;

    public AdminController(AdminTaskService adminTaskService) {
        this.adminTaskService = adminTaskService;
    }

    @Operation(summary = "Создание задачи",
            description = "Позволяет администратору создать новую задачу.")
    @PostMapping("/create-task")
    public ResponseEntity<TaskResponse> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания задачи")
            @Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        TaskResponse taskResponse = adminTaskService.createTask(taskCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @Operation(summary = "Получение задачи по ID",
            description = "Позволяет администратору получить задачу по её ID.")
    @GetMapping("/tasks/{task_id}")
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "ID задачи", example = "1")
            @PathVariable Long task_id) {
        TaskResponse taskResponse = adminTaskService.getTaskById(task_id);
        return ResponseEntity.ok(taskResponse);
    }

    @Operation(summary = "Получение списка всех задач",
            description = "Возвращает список всех задач в системе.")
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks() {
        List<TaskResponse> tasks = adminTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Удаление задачи",
            description = "Удаляет задачу по её ID.")
    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<MessageResponse> deleteTask(
            @Parameter(description = "ID задачи для удаления", example = "1")
            @PathVariable Long task_id) {
        adminTaskService.deleteTask(task_id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setText("task has been deleted!");
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(summary = "Обновление задачи",
            description = "Позволяет администратору обновить информацию о задаче.")
    @PatchMapping("/update/{task_id}")
    public ResponseEntity<TaskResponse> updateTask(
            @Parameter(description = "ID задачи для обновления", example = "1")
            @PathVariable Long task_id,
            @Valid @RequestBody TaskUpdateAdminDTO taskUpdateAdminDTO) {
        TaskResponse taskResponse = adminTaskService.updateTask(task_id, taskUpdateAdminDTO);
        return ResponseEntity.ok(taskResponse);
    }
}
