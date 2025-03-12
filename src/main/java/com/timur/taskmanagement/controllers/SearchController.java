package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.UserTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class SearchController {
    private final UserTaskService userTaskService;

    public SearchController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/search-tasks/{task_id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long task_id) throws AccessDeniedException {
        TaskResponse taskResponse = userTaskService.getTaskById(task_id);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/search-tasks/")
    public ResponseEntity<Page<TaskResponse>> getTasks(@RequestParam(required = false) Long authorId,
                                      @RequestParam(required = false) Long respUserId,
                                      @RequestParam int page,
                                      @RequestParam int size){

        Page<TaskResponse> taskResponses = userTaskService
                .getFiltrationTasks(authorId, respUserId, page, size);
        return ResponseEntity.ok(taskResponses);
    }
}
