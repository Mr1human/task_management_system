package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.CommentDTO;
import com.timur.taskmanagement.exceptions.NoAccessException;
import com.timur.taskmanagement.jwt.JwtUtils;
import com.timur.taskmanagement.models.Comment;
import com.timur.taskmanagement.models.Task;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.responses.CommentResponse;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentTaskService {

    private final CommentService commentService;
    private final TaskService taskService;
    private final UserService userService;
    private final AuthService authService;

    public CommentTaskService(CommentService commentService, TaskService taskService, UserService userService, AuthService authService) {
        this.commentService = commentService;
        this.taskService = taskService;
        this.userService = userService;
        this.authService = authService;
    }

    public List<CommentResponse> getCommentsTaskByTaskId(Long taskId) throws AccessDeniedException {
        User currentUser = authService.getCurrentUser();
        if (validateAccessUserToComments(taskId, currentUser)) {
            List<Comment> comments = commentService.getAllCommentsByTaskId(taskId);
            return comments.stream()
                    .map(comment -> {
                        CommentResponse commentResponse = new CommentResponse();
                        commentResponse.setText(comment.getText());
                        commentResponse.setTaskId(comment.getTask().getId());
                        commentResponse.setAuthorCommentId(comment.getAuthor().getId());
                        return commentResponse;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new NoAccessException("No Access!");
        }
    }

    public CommentResponse createComment(CommentDTO commentDTO) throws AccessDeniedException {
        User currentUser = authService.getCurrentUser();

        if (validateAccessUserToComments(commentDTO.getTaskId(), currentUser)) {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setTask(taskService.findById(commentDTO.getTaskId()));
            comment.setAuthor(currentUser);
            return commentToCommentResponse(commentService.save(comment));
        } else {
            throw new NoAccessException("No Access!");
        }

    }

    private CommentResponse commentToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setTaskId(comment.getTask().getId());
        commentResponse.setText(comment.getText());
        commentResponse.setAuthorCommentId(comment.getAuthor().getId());

        return commentResponse;
    }

    private boolean validateAccessUserToComments(Long taskId, User currentUser) {
        Task task = taskService.findById(taskId);
        Long userTaskId = task.getRespUser().getId();
        if (task == null || task.getRespUser() == null) {
            throw new EntityNotFoundException("Task or responsible user not found");
        }
        return currentUser.getId().equals(userTaskId) || userService.isAdmin(currentUser);
    }
}