package com.example.task.service;

import com.example.task.model.Comment;
import com.example.task.model.Priority;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CustomTaskService customTaskService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(Status.PENDING);
        task.setPriority(Priority.LOW);
        task.setAssignees(new ArrayList<>());
        task.setComments(new ArrayList<>());

        User author = new User();
        author.setId(1L);
        task.setAuthor(author);
    }

    @Test
    void save_ShouldSaveTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = customTaskService.save(task);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void update_ShouldUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task updatedTask = customTaskService.update("Updated Title", "Updated Description", 1L);

        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            customTaskService.update("Title", "Description", 1L);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());
    }

    @Test
    void delete_ShouldDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        Long deletedId = customTaskService.delete(1L);

        assertEquals(1L, deletedId);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = customTaskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void setStatus_ShouldSetTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task updatedTask = customTaskService.setStatus(Status.IN_PROGRESS, 1L);

        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void setStatus_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            customTaskService.setStatus(Status.IN_PROGRESS, 1L);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());
    }

    @Test
    void setComment_ShouldAddCommentToTask() {
        Comment comment = new Comment ();
        comment.setId(1L);
        comment.setText("This is a comment");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Comment addedComment = customTaskService.setComment(comment, 1L);

        assertEquals(comment, addedComment);
        assertEquals(1, task.getComments().size());
        assertEquals("This is a comment", task.getComments().get(0).getText());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void setComment_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("This is a comment");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            customTaskService.setComment(comment, 1L);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());
    }

    @Test
    void isAssignee_ShouldReturnTrue_WhenUserIsAssignee() {
        User user = new User();
        user.setId(1L);
        task.getAssignees().add(user);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        boolean result = customTaskService.isAssignee(task.getId(), user.getId());

        assertTrue(result);
    }

    @Test
    void isAuthor_ShouldReturnFalse_WhenUserIsNotAuthor() {
        User author = new User();
        author.setId(2L);
        User currentAuthor = new User();
        currentAuthor.setId(1L);
        task.setAuthor(currentAuthor);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    
        boolean result = customTaskService.isAuthor(task.getId(), author.getId());
    
        assertFalse(result);
    }
    
    @Test
    void isAssignee_ShouldReturnFalse_WhenUserIsNotAssignee() {
        User user = new User();
        user.setId(2L);
        User assignee = new User();
        assignee.setId(1L);
        task.getAssignees().add(assignee);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    
        boolean result = customTaskService.isAssignee(task.getId(), user.getId());
    
        assertFalse(result);
    }

    @Test
    void isAuthor_ShouldReturnTrue_WhenUserIsAuthor() {
        User author = new User();
        author.setId(1L);
        task.setAuthor(author);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        boolean result = customTaskService.isAuthor(task.getId(), author.getId());

        assertTrue(result);
    }
}