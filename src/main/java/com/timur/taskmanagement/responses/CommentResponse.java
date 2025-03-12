package com.timur.taskmanagement.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentResponse {
    private String text;
    private Long taskId;
    private Long authorCommentId;
}
