package com.example.task.service;

import com.example.task.model.dto.JwtAuthenticationResponse;
import com.example.task.model.dto.SignInRequest;
import com.example.task.model.dto.SignUpRequest;
import com.example.task.service.CustomUserService.UserAlreadyExistsException;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request) throws UserAlreadyExistsException ;

    JwtAuthenticationResponse signIn(SignInRequest request);
}
