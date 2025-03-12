package com.timur.taskmanagement.responses;

import com.timur.taskmanagement.enums.TaskPriority;
import com.timur.taskmanagement.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long authorId;
    private Long respUserId;
}
