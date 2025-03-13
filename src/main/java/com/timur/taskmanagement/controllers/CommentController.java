package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.CommentDTO;
import com.timur.taskmanagement.responses.CommentResponse;
import com.timur.taskmanagement.services.CommentTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@Tag(name = "Комментарии", description = "Контроллер для работы с комментариями к задачам")
public class CommentController {
    private final CommentTaskService commentTaskService;

    public CommentController(CommentTaskService commentTaskService) {
        this.commentTaskService = commentTaskService;
    }

    @Operation(summary = "Создание комментария",
            description = "Позволяет пользователю добавить комментарий к той задаче, где он является исполнителем")
    @PostMapping("/create-comment")
    public ResponseEntity<CommentResponse> createComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные комментария")
            @RequestBody CommentDTO commentDTO) throws AccessDeniedException {

        CommentResponse commentResponse = commentTaskService.createComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @Operation(summary = "Получение комментариев к задаче",
            description = "Возвращает список комментариев для конкретной задачи. " +
                    "Просматривать комментарии может автор задачи (администратор) и исполнитель")
    @GetMapping("/comments/{task_id}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "ID задачи", example = "1")
            @PathVariable Long task_id) throws AccessDeniedException {
        List<CommentResponse> commentDTOS =
                commentTaskService.getCommentsTaskByTaskId(task_id);
        return ResponseEntity.ok(commentDTOS);
    }
}
