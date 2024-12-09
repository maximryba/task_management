package com.example.task.model.dto;

import com.example.task.model.Priority;
import com.example.task.model.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Ответ на создание задачи")
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private Long authorId;
}

