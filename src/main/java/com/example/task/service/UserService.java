package com.example.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.task.model.User;
import com.example.task.service.CustomUserService.UserAlreadyExistsException;

public interface UserService {

   User save(User user);

   User create(User user) throws UserAlreadyExistsException;

   UserDetailsService userDetailsService();

   User getByEmail(String email);

   Optional<User> getById(Long id);

    List<User> getAllByIds(List<Long> ids);

}