package com.example.task.model.dto;

import com.example.task.model.Priority;
import com.example.task.validation.ValidPriority;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание задачи")
public class TaskCreateRequest {

    @Schema(description = "Название задачи", example = "Task 1")
    @Size(min = 1, max = 255, message = "Название задачи должно содержать от 1 до 255 символов")
    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    @Schema(description = "Описание задачи", example = "Some description")
    @Size(min = 1, max = 1000, message = "Описание задачи должно содержать от 1 до 1000 символов")
    @NotBlank(message = "Описание задачи не может быть пустыми")
    private String description;

    @Schema(description = "Приоритет", example = "MEDIUM")
    @ValidPriority
    private Priority priority;

}
