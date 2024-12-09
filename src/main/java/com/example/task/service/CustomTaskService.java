package com.example.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.task.model.Comment;
import com.example.task.model.Priority;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task save(Task task) {
        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task update(String title, String description, Long id) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));

        existingTask.setTitle(title);
        existingTask.setDescription(description);

        return existingTask;
    }

    @Override
    @Transactional
    public Long delete(Long id) {
        this.taskRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task setStatus(Status status, Long id) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));

        existingTask.setStatus(status);

        return existingTask;
    }

    @Override
    @Transactional
    public Task setPriority(Priority priority, Long id) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));

        existingTask.setPriority(priority);

        return existingTask;
    }

    @Override
    @Transactional
    public Task setAssignees(List<User> assignees, Long id) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Задача с id " + id + " не найдена"));

        existingTask.setAssignees(assignees);

        return existingTask;
    }

    @Override
    @Transactional
    public Comment setComment(Comment comment, Long taskId) {
        Task existingTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));
        List<Comment> comments = existingTask.getComments();
        comments.add(comment);

        existingTask.setComments(comments);

        return comment;
    }

    @Override
    public Page<Task> getTasksByAuthor(Long authorId, Priority priority, Status status, Pageable pageable) {
        return this.taskRepository.findByAuthor(authorId, status, priority, pageable);
    }

    @Override
    public Page<Task> getTasksByAssignee(Long assigneeId, Priority priority, Status status, Pageable pageable) {
        return this.taskRepository.findByAssignee(assigneeId, status, priority, pageable);
    }

    @Override
    public Optional<Task> getById(Long id) {
        return this.taskRepository.findById(id);
    }

    @Override
    public boolean isAssignee(Long taskId, Long userId) {
        Task task = this.getById(taskId).orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));

        return task.getAssignees().stream()
            .anyMatch(assignee -> assignee.getId().equals(userId));
    }

    @Override
    public boolean isAuthor(Long taskId, Long userId) {
        Task task = this.getById(taskId).orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));

        return task.getAuthor().getId().equals(userId);
    }

}
