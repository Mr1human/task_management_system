package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.jwt.JwtUtils;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class UserTaskService {
    private final TaskService taskService;
    private final JwtUtils jwtUtils;

    public UserTaskService(TaskService taskService, JwtUtils jwtUtils) {
        this.taskService = taskService;
        this.jwtUtils = jwtUtils;
    }

    public TaskDTO getTaskById(Long taskId, String authorizationHeader) throws AccessDeniedException {
        Task task = taskService.findById(taskId);

        if(!validateUserAccessToTask(task, authorizationHeader)){
            throw new AccessDeniedException("No access!");
        }

        return taskService.taskToTaskDTO(task);
    }

    public TaskDTO updateTask(Long taskId, TaskUpdateUserDTO taskUpdateUserDTO, String authorizationHeader) throws AccessDeniedException {
        Task task = taskService.findById(taskId);

        if(!validateUserAccessToTask(task, authorizationHeader)){
            throw new AccessDeniedException("No access!");
        }

        if(taskUpdateUserDTO.getStatus()!=null){
            task.setStatus(taskUpdateUserDTO.getStatus());
        }

        return taskService.taskToTaskDTO(taskService.save(task));
    }

    private boolean validateUserAccessToTask (Task task, String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");
        Claims claims = jwtUtils.getClaimsFromJwtAccessToken(token);
        Long currentUserId = Long.parseLong(claims.get("userId", String.class));

        return task.getRespUser().getId().equals(currentUserId);
    }
}
