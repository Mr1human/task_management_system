package com.timur.taskmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentDTO {
    private String text;
    private Long taskId;
}
