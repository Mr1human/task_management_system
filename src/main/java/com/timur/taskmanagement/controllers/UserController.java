package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.TaskService;
import com.timur.taskmanagement.services.UserTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class UserController {
    private final UserTaskService userTaskService;

    public UserController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @PatchMapping("/update/{task_id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long task_id,
                                        @RequestBody TaskUpdateUserDTO taskUpdateUserDTO) throws AccessDeniedException {
       TaskResponse taskResponse = userTaskService
               .updateTask(task_id, taskUpdateUserDTO);
       return ResponseEntity.ok(taskResponse);
    }
}
