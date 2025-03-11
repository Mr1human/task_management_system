package com.timur.taskmanagement.dto;

import com.timur.taskmanagement.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class TaskUpdateUserDTO {
    private TaskStatus status;
}
