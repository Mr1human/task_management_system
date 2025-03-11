package com.timur.taskmanagement.dto;

import com.timur.taskmanagement.enums.TaskPriority;
import com.timur.taskmanagement.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter @Getter
public class TaskUpdateAdminDTO {
    private String tittle;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long respUserId;
}
