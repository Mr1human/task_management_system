package com.timur.taskmanagement.repositories;

import com.timur.taskmanagement.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByTaskId(Long taskId);
}
