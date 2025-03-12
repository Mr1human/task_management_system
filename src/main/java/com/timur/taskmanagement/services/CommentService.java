package com.timur.taskmanagement.services;

import com.timur.taskmanagement.models.Comment;
import com.timur.taskmanagement.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;

    public CommentService(CommentRepository commentRepository, TaskService taskService) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
    }

    public Comment save(Comment comment){
        return  commentRepository.save(comment);
    }

    public void deleteById(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getAllCommentsByTaskId(Long taskId){
        return commentRepository.findCommentsByTaskId(taskId);
    }
}
