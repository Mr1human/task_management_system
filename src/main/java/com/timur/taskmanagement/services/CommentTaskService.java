package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.CommentDTO;
import com.timur.taskmanagement.models.Comment;

public class TaskCommentService {
    private final CommentService commentService;

    public TaskCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public Comment createComment(CommentDTO commentDTO){

    }
}
