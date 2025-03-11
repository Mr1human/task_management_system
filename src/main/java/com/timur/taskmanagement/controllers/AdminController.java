package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.services.AdminTaskService;
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

    @PostMapping("/createtask")
    public ResponseEntity<?> createTask(@RequestBody TaskCreateDTO taskCreateDTO){
        TaskDTO taskDTO = adminTaskService.createTask(taskCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @GetMapping("/task/{task_id}")
    public ResponseEntity<?> getTask(@PathVariable Long task_id){
        TaskDTO taskDTO = adminTaskService.getTaskById(task_id);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(){
        List<TaskDTO> tasks = adminTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long task_id){
        adminTaskService.deleteTask(task_id);
        return ResponseEntity.ok("task has been deleted!");
    }

    @PatchMapping("/update/{task_id}")
    public ResponseEntity<?> updateTask(@PathVariable Long task_id,
                                        @RequestBody TaskUpdateAdminDTO taskUpdateAdminDTO){
        TaskDTO taskDTO = adminTaskService.updateTask(task_id, taskUpdateAdminDTO);
        return ResponseEntity.ok(taskDTO);
    }
}
