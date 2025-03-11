package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.services.TaskService;
import com.timur.taskmanagement.services.UserTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class UserController {
    private final UserTaskService userTaskService;

    public UserController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/task/{task_id}")
    public ResponseEntity<?> getTask(@PathVariable Long task_id,
                                     @RequestHeader("Authorization") String authorizationHeader) throws AccessDeniedException {
        TaskDTO taskDTO = userTaskService.getTaskById(task_id, authorizationHeader);
        return ResponseEntity.ok(taskDTO);
    }

    @PatchMapping("/update/{task_id}")
    public ResponseEntity<?> updateTask(@PathVariable Long task_id,
                                        @RequestBody TaskUpdateUserDTO taskUpdateUserDTO,
                                        @RequestHeader("Authorization") String authorizationHeader) throws AccessDeniedException {
       TaskDTO taskDTO = userTaskService
               .updateTask(task_id, taskUpdateUserDTO, authorizationHeader);
       return ResponseEntity.ok(taskDTO);
    }
}
