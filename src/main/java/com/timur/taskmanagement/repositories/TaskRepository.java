package com.timur.taskmanagement.repositories;

import com.timur.taskmanagement.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthor_Id(Long authorId, Pageable pageable);
    Page<Task> findByRespUser_Id(Long respUserId, Pageable pageable);
}
