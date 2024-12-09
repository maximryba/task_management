package com.example.task.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.model.Comment;
import com.example.task.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Управление комментариями")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get/{taskId}")
    public List<Comment> getAllByTaskId(@PathVariable Long taskId) {
        return this.commentService.getCommentsByTaskId(taskId);
    }

}
