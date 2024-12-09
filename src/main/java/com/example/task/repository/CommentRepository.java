package com.example.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByTaskId(Long taskId);
}
