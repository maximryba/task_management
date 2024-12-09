package com.example.task.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.task.model.Role;
import com.example.task.model.User;
import com.example.task.model.dto.JwtAuthenticationResponse;
import com.example.task.model.dto.SignInRequest;
import com.example.task.model.dto.SignUpRequest;
import com.example.task.security.JwtTokenProvider;
import com.example.task.service.CustomUserService.UserAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationService implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) throws UserAlreadyExistsException {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        String jwt = jwtTokenProvider.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Неверные email или пароль");
        }

        UserDetails user = this.userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());

        String jwt = jwtTokenProvider.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

}
