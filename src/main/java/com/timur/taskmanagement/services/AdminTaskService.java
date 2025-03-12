package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.responses.TaskResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminTaskService {
    private final UserService userService;
    private final TaskService taskService;

    public AdminTaskService(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    public TaskResponse createTask(TaskCreateDTO taskCreateDTO){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = userService.findUserByEmail(currentUser.getUsername());

        Task task = new Task();
        task.setTittle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setStatus(taskCreateDTO.getStatus());
        task.setPriority(taskCreateDTO.getPriority());
        task.setAuthor(author);
        task.setRespUser(userService.findUserById(taskCreateDTO.getRespUserId()));

        return taskService.taskToTaskResponse(taskService.save(task));
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateAdminDTO taskUpdateAdminDTO){
        Task task = taskService.updateTaskForAdmin(taskId, taskUpdateAdminDTO);
        return taskService.taskToTaskResponse(task);
    }

    public List<TaskResponse> getAllTasks(){
        List<Task> tasks = taskService.getAllTasksForAdmin();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task:tasks){
            TaskResponse taskResponse = taskService.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    public void deleteTask(Long id){
        taskService.deleteById(id);
    }

    public TaskResponse getTaskById(Long id){
        Task task = taskService.findById(id);
        return taskService.taskToTaskResponse(task);
    }
}
