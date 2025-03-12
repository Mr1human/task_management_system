package com.timur.taskmanagement.controllers;

import com.timur.taskmanagement.dto.CommentDTO;
import com.timur.taskmanagement.responses.CommentResponse;
import com.timur.taskmanagement.services.CommentTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class CommentController {
    private final CommentTaskService commentTaskService;

    public CommentController(CommentTaskService commentTaskService) {
        this.commentTaskService = commentTaskService;
    }

    @PostMapping("/create-comment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentDTO commentDTO,
                                        @RequestHeader("Authorization") String authorizationHeader) throws AccessDeniedException {

        CommentResponse commentResponse = commentTaskService.createComment(commentDTO, authorizationHeader);
        return ResponseEntity.ok(commentResponse);
    }

    @GetMapping("/comments/{task_id}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long task_id,
                                         @RequestHeader("Authorization") String authorizationHeader) throws AccessDeniedException {
        List<CommentResponse> commentDTOS =
                commentTaskService.getCommentsTaskByTaskId(task_id, authorizationHeader);
        return ResponseEntity.ok(commentDTOS);
    }
}
