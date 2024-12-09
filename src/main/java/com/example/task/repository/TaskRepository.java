package com.example.task.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.task.model.Priority;
import com.example.task.model.Status;
import com.example.task.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    @Query("SELECT t FROM Task t WHERE t.author.id = :authorId " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority)")
    Page<Task> findByAuthor(
        @Param("authorId") Long authorId,
        @Param("status") Status status,
        @Param("priority") Priority priority,
        Pageable pageable
    );

    @Query("SELECT t FROM Task t JOIN t.assignees a WHERE a.id = :assigneeId " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority)")
    Page<Task> findByAssignee(
        @Param("assigneeId") Long assigneeId,
        @Param("status") Status status,
        @Param("priority") Priority priority,
        Pageable pageable
    );
}
