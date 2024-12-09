package com.example.task.controller;

import com.example.task.model.Priority;
import com.example.task.model.Role;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.security.JwtTokenProvider;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "ADMIN")
    void shouldUpdateTaskSuccessfully() throws Exception {
        Task mockTask = Task.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .priority(Priority.LOW)
                .status(Status.PENDING)
                .author(new User(1L, "user1", "user@example.com", "password", Role.ROLE_ADMIN, null, null))
                .build();

        Mockito.when(taskService.update(eq("Updated Title"), eq("Updated Description"), eq(1L)))
                .thenReturn(mockTask);

        mockMvc.perform(patch("/api/tasks/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }


    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void shouldDeleteTaskSuccessfully() throws Exception {
        Mockito.when(taskService.delete(1L)).thenReturn(1L);

        mockMvc.perform(delete("/api/tasks/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}

