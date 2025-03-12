package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.responses.MessageResponse;
import com.timur.taskmanagement.responses.TaskResponse;
import com.timur.taskmanagement.services.AdminTaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminTaskService adminTaskService;

    public AdminController(AdminTaskService adminTaskService) {
        this.adminTaskService = adminTaskService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(){
        return ResponseEntity.ok("OK!");
    }


    @PostMapping("/create-task")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO){
        TaskResponse taskResponse = adminTaskService.createTask(taskCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @GetMapping("/tasks/{task_id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long task_id){
        TaskResponse taskResponse = adminTaskService.getTaskById(task_id);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(){
        List<TaskResponse> tasks = adminTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<MessageResponse> deleteTask(@PathVariable Long task_id){
        adminTaskService.deleteTask(task_id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setText("task has been deleted!");
        return ResponseEntity.ok(messageResponse);
    }

    @PatchMapping("/update/{task_id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long task_id,
                                        @Valid @RequestBody TaskUpdateAdminDTO taskUpdateAdminDTO){
        TaskResponse taskResponse = adminTaskService.updateTask(task_id, taskUpdateAdminDTO);
        return ResponseEntity.ok(taskResponse);
    }
}
