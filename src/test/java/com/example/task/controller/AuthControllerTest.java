package com.example.task.controller;

import com.example.task.model.dto.JwtAuthenticationResponse;
import com.example.task.model.dto.SignUpRequest;
import com.example.task.service.AuthenticationService;
import com.example.task.service.CustomUserService.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldSignUpSuccessfully() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Jonny", "jondoe@gmail.com", "my_1secret1_password");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("fake-jwt-token");

        when(authenticationService.signUp(signUpRequest)).thenReturn(response);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Jonny\", \"email\":\"jondoe@gmail.com\", \"password\":\"my_1secret1_password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void shouldReturnBadRequestWhenUserAlreadyExists() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Jon", "jondoe@gmail.com", "my_1secret1_password");

        doThrow(new UserAlreadyExistsException("User already exists")).when(authenticationService).signUp(signUpRequest);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Jon\", \"email\":\"jondoe@gmail.com\", \"password\":\"my_1secret1_password\"}"))
                .andExpect(status().isBadRequest());
    }
}
