package com.example.task.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.model.Comment;
import com.example.task.model.Priority;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.model.dto.CommentCreateRequest;
import com.example.task.model.dto.TaskCreateRequest;
import com.example.task.model.dto.TaskResponse;
import com.example.task.model.dto.TaskUpdateRequest;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {

    private final TaskService taskService;

    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Создание задачи")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskCreateRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {
            Long userId = this.userService.getByEmail(userDetails.getUsername()).getId();
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(Status.PENDING)
                .author(this.userService.getById(userId).orElseThrow(() -> 
                    new EntityNotFoundException("Автор с ID " + userId + " не найден")))
                .build();

        Task savedTask = taskService.save(task);

        TaskResponse response = TaskResponse.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .priority(savedTask.getPriority())
                .status(savedTask.getStatus())
                .authorId(userId)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAssignee(#id, authentication.principal.id)" +
     " or @customTaskService.isAuthor(#id, authentication.principal.id)")
    @Operation(summary = "Получение задачи по ID")
    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long id) {
        return ResponseEntity.ok().body(this.taskService.getById(id).orElseThrow(() ->
         new EntityNotFoundException("Задача с ID " + id + " не найдена")));
    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAuthor(#id, authentication.principal.id)")
    @Operation(summary = "Изменение задачи")
    @PatchMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody @Valid TaskUpdateRequest request, 
        @PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long id) {
            return ResponseEntity.ok().body(this.taskService.update(request.getTitle(), request.getDescription(), id));

    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAssignee(#id, authentication.principal.id)" +
     " or @customTaskService.isAuthor(#id, authentication.principal.id)")
    @Operation(summary = "Установка статуса задачи")
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Task> setStatus(@RequestBody Status status, 
        @PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long id) {
        return ResponseEntity.ok().body(this.taskService.setStatus(status, id));
    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAuthor(#id, authentication.principal.id)")
    @Operation(summary = "Установка приоритета задачи")
    @PatchMapping("/set-priority/{id}")
    public ResponseEntity<Task> setPriority(@RequestBody Priority priority, 
        @PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long id) {
        return ResponseEntity.ok().body(this.taskService.setPriority(priority, id));
    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAssignee(#taskId, authentication.principal.id)" +
     " or @customTaskService.isAuthor(#taskId, authentication.principal.id)")
    @Operation(summary = "Оставить комментарий к задаче")
    @PatchMapping("/set-comment/{taskId}")
    public ResponseEntity<Comment> setComment(@RequestBody @Valid CommentCreateRequest request,
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long taskId) {
            Comment comment = Comment.builder()
            .author(this.userService.getByEmail(userDetails.getUsername()))
            .text(request.getText())
            .task(this.taskService.getById(taskId).orElseThrow(() -> 
                new EntityNotFoundException("Задача с ID " + taskId + " не найдена")))
            .build();

            return ResponseEntity.ok().body(this.taskService.setComment(comment, taskId));
    }

    @PreAuthorize("hasRole('ADMIN') or @customTaskService.isAuthor(#taskId, authentication.principal.id)")
    @Operation(summary = "Установить исполнителей задачи")
    @PatchMapping("/set-assignees/{taskId}")
    public ResponseEntity<Task> setAuthor(@RequestBody List<Long> assigneesIds, 
        @PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long taskId) {
        List<User> assignees = this.userService.getAllByIds(assigneesIds);
        return ResponseEntity.ok().body(this.taskService.setAssignees(assignees, taskId));
    }

    @Operation(summary = "Получение задач по автору")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<Task>> getTasksByAuthor(
            @PathVariable @Min(value = 1, message = "ID автора должно быть положительным числом") Long authorId,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status,
            @Valid Pageable pageable) {
        
        Page<Task> tasks = taskService.getTasksByAuthor(authorId, priority, status, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получение задач по исполнителю")
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<Task>> getTasksByAssignee(
            @PathVariable @Min(value = 1, message = "ID исполнителя должно быть положительным числом") Long assigneeId,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status,
            @Valid Pageable pageable) {

        Page<Task> tasks = taskService.getTasksByAssignee(assigneeId, priority, status, pageable);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление задачи по ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteTask(@PathVariable @Min(value = 1, message = "ID задачи должно быть положительным числом") Long id) {
        return ResponseEntity.ok().body(this.taskService.delete(id));
    }

}
