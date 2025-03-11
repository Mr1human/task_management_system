package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.TaskDTO;
import com.timur.taskmanagement.dto.TaskUpdateAdminDTO;
import com.timur.taskmanagement.dto.TaskUpdateUserDTO;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.TaskRepository;
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
                .orElseThrow(()-> new RuntimeException("Task with id was not found!"));
    }

    public List<Task> getAllTasksForAdmin(){
        return taskRepository.findAll();
    }

    public Task updateTaskForAdmin(Long taskId, TaskUpdateAdminDTO taskUpdateAdminDTO){
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(()-> new RuntimeException("Task with this ID was not found"));
        if(taskUpdateAdminDTO.getTittle()!=null){
            task.setTittle(taskUpdateAdminDTO.getTittle());
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
                .orElseThrow(()-> new RuntimeException("Task with this ID was not found"));
        if(taskUpdateUserDTO.getStatus()!=null){
            task.setStatus(taskUpdateUserDTO.getStatus());
        }
        return taskRepository.save(task);
    }

    public TaskDTO taskToTaskDTO(Task task){
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(task.getId());
        taskDTO.setTittle(task.getTittle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setAuthorId(task.getAuthor().getId());
        taskDTO.setRespUserId(task.getRespUser().getId());

        return taskDTO;
    }
}
