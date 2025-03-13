package com.timur.taskmanagement.dto;

import com.timur.taskmanagement.enums.TaskPriority;
import com.timur.taskmanagement.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class TaskCreateDTO {
    @NotNull(message = "Title must not be empty")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private TaskPriority priority;

    @NotNull(message = "Responsible user is required")
    @Positive(message = "Responsible user ID must be a positive number")
    private Long respUserId;
}
