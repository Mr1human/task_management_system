package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskCreateDTO;
import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
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

    public TaskDTO createTask(TaskCreateDTO taskCreateDTO){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = userService.findUserByEmail(currentUser.getUsername());

        Task task = new Task();
        task.setTittle(taskCreateDTO.getTittle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setStatus(taskCreateDTO.getStatus());
        task.setPriority(taskCreateDTO.getPriority());
        task.setAuthor(author);
        task.setRespUser(userService.findUserById(taskCreateDTO.getRespUserId()));

        return taskService.taskToTaskDTO(taskService.save(task));
    }

    public TaskDTO updateTask(Long taskId, TaskUpdateAdminDTO taskUpdateAdminDTO){
        Task task = taskService.updateTaskForAdmin(taskId, taskUpdateAdminDTO);
        return taskService.taskToTaskDTO(task);
    }

    public List<TaskDTO> getAllTasks(){
        List<Task> tasks = taskService.getAllTasksForAdmin();
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task:tasks){
            TaskDTO taskDTO = taskService.taskToTaskDTO(task);
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }

    public void deleteTask(Long id){
        taskService.deleteById(id);
    }

    public TaskDTO getTaskById(Long id){
        Task task = taskService.findById(id);
        return taskService.taskToTaskDTO(task);
    }
}
