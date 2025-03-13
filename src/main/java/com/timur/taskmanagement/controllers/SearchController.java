package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@Tag(name = "Поиск задач", description = "Контроллер для поиска и фильтрации задач")
public class SearchController {
    private final UserTaskService userTaskService;

    public SearchController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @Operation(summary = "Получение задачи по ID", description = "Возвращает задачу по её ID")
    @GetMapping("/search-tasks/{task_id}")
    public ResponseEntity<TaskResponse> getTask(@Parameter(description = "ID задачи", example = "1")
                                                @PathVariable Long task_id) throws AccessDeniedException {
        TaskResponse taskResponse = userTaskService.getTaskById(task_id);
        return ResponseEntity.ok(taskResponse);
    }
    @Operation(summary = "Фильтрация и пагинация задач по автору", description = "Позволяет фильтровать задачи по автору")
    @GetMapping("/search-tasks/by-author")
    public ResponseEntity<Page<TaskResponse>> getTasksByAuthor(
            @RequestParam Long authorId,
            @RequestParam int page,
            @RequestParam int size) {

        Page<TaskResponse> taskResponses = userTaskService.getTasksByAuthor(authorId, page, size);
        return ResponseEntity.ok(taskResponses);
    }

    @Operation(summary = "Фильтрация и пагинация задач по исполнителю", description = "Позволяет фильтровать задачи по исполнителю")
    @GetMapping("/search-tasks/by-resp-user")
    public ResponseEntity<Page<TaskResponse>> getTasksByRespUser(
            @RequestParam Long respUserId,
            @RequestParam int page,
            @RequestParam int size) {

        Page<TaskResponse> taskResponses = userTaskService.getTasksByRespUserID(respUserId, page, size);
        return ResponseEntity.ok(taskResponses);
    }
}
