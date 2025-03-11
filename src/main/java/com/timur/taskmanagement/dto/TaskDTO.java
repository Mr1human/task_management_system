package com.timur.taskmanagement.dto;

import com.timur.taskmanagement.enums.TaskPriority;
import com.timur.taskmanagement.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskDTO {
    private Long id;
    private String tittle;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long authorId;
    private Long respUserId;

}
