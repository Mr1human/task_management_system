package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.exceptions.NoAccessException;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class UserTaskService {
    private final TaskService taskService;
    private final AuthService authService;
    private final UserService userService;

    public UserTaskService(TaskService taskService, AuthService authService, UserService userService) {
        this.taskService = taskService;
        this.authService = authService;
        this.userService = userService;
    }

    public TaskDTO getTaskById(Long taskId, String authorizationHeader) throws AccessDeniedException {
        Task task = taskService.findById(taskId);
        User currentUser = authService.getCurrentUserFromToken(authorizationHeader);

        if(!taskService.validateAccessUserToTask(task.getId(), currentUser)){
            throw new AccessDeniedException("No access!");
        }

        return taskService.taskToTaskDTO(task);
    }

    public Page<TaskDTO> getFiltrationTasks(Long authorId, Long respUserId, int page, int size, String authorizationHeader) {
        if (authorId == null && respUserId == null) {
            throw new IllegalArgumentException("Either authorId or respUserId must be provided");
        } else if (authorId != null) {
            return getTasksByAuthor(authorId, page, size, authorizationHeader);
        } else {
            return getTasksByRespUserID(respUserId, page, size, authorizationHeader);
        }
    }

    public TaskDTO updateTask(Long taskId, TaskUpdateUserDTO taskUpdateUserDTO, String authorizationHeader) throws AccessDeniedException {
        Task task = taskService.findById(taskId);
        User currentUser = authService.getCurrentUserFromToken(authorizationHeader);

        if(!taskService.validateAccessUserToTask(task.getId(), currentUser)){
            throw new NoAccessException("No access!");
        }

        if(taskUpdateUserDTO.getStatus()!=null){
            task.setStatus(taskUpdateUserDTO.getStatus());
        }

        return taskService.taskToTaskDTO(taskService.save(task));
    }

    private Page<TaskDTO> getTasksByAuthor(Long authorId, int page, int size, String authorization){
        User currentUser = authService.getCurrentUserFromToken(authorization);

        if (currentUser.getId().equals(authorId) || userService.isAdmin(currentUser)){
            Page<Task> taskPage = taskService.getTasksByAuthor(authorId, page, size);
            return taskPage.map(task -> taskService.taskToTaskDTO(task));
        }
        throw new NoAccessException("No Access");
    }

    private Page<TaskDTO> getTasksByRespUserID(Long respUserId, int page, int size, String authorization){
        User currentUser = authService.getCurrentUserFromToken(authorization);

        if (currentUser.getId().equals(respUserId) || userService.isAdmin(currentUser)){
            Page<Task> taskPage = taskService.getTasksByRespUser(respUserId, page, size);
            return taskPage.map(task -> taskService.taskToTaskDTO(task));
        }
        throw new NoAccessException("No Access");
    }
}
