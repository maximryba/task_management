package com.example.task.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание комментария")
public class CommentCreateRequest {

    @Schema(description = "Текст комментария", example = "Some comment")
    @Size(min = 1, max = 1000, message = "Текст комментария должен содержать от 1 до 1000 символов")
    @NotBlank(message = "Название задачи не может быть пустым")
    private String text;

}
