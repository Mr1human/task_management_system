package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.services.UserTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class SearchTasks {
    private final UserTaskService userTaskService;

    public SearchTasks(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/search-tasks/")
    public ResponseEntity<?> getTasks(@RequestParam(required = false) Long authorId,
                                      @RequestParam(required = false) Long respUserId,
                                      @RequestParam int page,
                                      @RequestParam int size,
                                      @RequestHeader("Authorization") String authorizationHeader) throws AccessDeniedException {

        Page<TaskDTO> taskDTOPage = userTaskService
                .getFiltrationTasks(authorId, respUserId, page, size, authorizationHeader);
        return ResponseEntity.ok(taskDTOPage);
    }
}
