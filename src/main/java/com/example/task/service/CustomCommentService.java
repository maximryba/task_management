package com.example.task.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task.model.Comment;
import com.example.task.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomCommentService implements CommentService {
    
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByTaskId(Long taskId) {
        return this.commentRepository.findByTaskId(taskId);
    }

}
