package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.TaskRepository;
import com.timur.taskmanagement.responses.TaskResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task save(Task task){
        return taskRepository.save(task);
    }

    public void deleteById(Long id){
        taskRepository.deleteById(id);
    }

    public Task findById(Long id){
        return taskRepository
                .findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Task with id was not found!"));
    }

    public List<Task> getAllTasksForAdmin(){
        return taskRepository.findAll();
    }

    public Page<Task> getTasksByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByAuthor_Id(authorId, pageable);
    }

    public Page<Task> getTasksByRespUser(Long respUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByRespUser_Id(respUserId, pageable);
    }

    public Task updateTaskForAdmin(Long taskId, TaskUpdateAdminDTO taskUpdateAdminDTO){
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Task with this ID was not found"));
        if(taskUpdateAdminDTO.getTitle()!=null){
            task.setTittle(taskUpdateAdminDTO.getTitle());
        }
        if(taskUpdateAdminDTO.getDescription()!=null){
            task.setDescription(taskUpdateAdminDTO.getDescription());
        }
        if(taskUpdateAdminDTO.getPriority()!=null){
            task.setPriority(taskUpdateAdminDTO.getPriority());
        }
        if(taskUpdateAdminDTO.getStatus()!=null){
            task.setStatus(taskUpdateAdminDTO.getStatus());
        }
        if(taskUpdateAdminDTO.getRespUserId()!=null){
            User user = userService.findUserById(taskUpdateAdminDTO.getRespUserId());
            task.setRespUser(user);
        }
        return taskRepository.save(task);
    }

    public Task updateTaskForUser(Long taskId, TaskUpdateUserDTO taskUpdateUserDTO){
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Task with this ID was not found"));
        if(taskUpdateUserDTO.getStatus()!=null){
            task.setStatus(taskUpdateUserDTO.getStatus());
        }
        return taskRepository.save(task);
    }

    public boolean validateAccessUserToTask(Long taskId, User currentUser) {
        Task task = findById(taskId);
        Long userTaskId = task.getRespUser().getId();
        if (task == null || task.getRespUser() == null) {
            throw new EntityNotFoundException("Task or responsible user not found");
        }
        System.out.println("Current user ID: " + currentUser.getId());
        System.out.println("Task responsible user ID: " + userTaskId);
        System.out.println("Is admin: " + userService.isAdmin(currentUser));
        return currentUser.getId().equals(userTaskId) || userService.isAdmin(currentUser);
    }

    public TaskResponse taskToTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTittle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setAuthorId(task.getAuthor().getId());
        taskResponse.setRespUserId(task.getRespUser().getId());

        return taskResponse;
    }
}
