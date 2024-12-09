package com.example.task.service;

import java.util.List;

import com.example.task.model.Comment;

public interface CommentService {

    List<Comment> getCommentsByTaskId(Long taskId);

}
