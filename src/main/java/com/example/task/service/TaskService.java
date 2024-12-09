package com.example.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.task.model.Comment;
import com.example.task.model.Priority;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;

public interface TaskService {

    Task save(Task task);

    Task update(String title, String description, Long id);

    Long delete(Long id);

    List<Task> getAllTasks();

    Task setStatus(Status status, Long id);

    Task setPriority(Priority priority, Long id);

    Task setAssignees(List<User> assignees, Long id);

    Comment setComment(Comment comment, Long taskId);

    Page<Task> getTasksByAuthor(Long authorId, Priority priority, Status status, Pageable pageable);

    Page<Task> getTasksByAssignee(Long assigneeId, Priority priority, Status status, Pageable pageable);

    Optional<Task> getById(Long id);

    boolean isAssignee(Long taskId, Long userId);

    boolean isAuthor(Long taskId, Long userId);

}
