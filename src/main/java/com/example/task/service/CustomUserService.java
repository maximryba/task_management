package com.example.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.task.model.User;
import com.example.task.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserService implements UserService, UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким email не найден: " + email));
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User create(User user) throws UserAlreadyExistsException {
        if (this.userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    @Override
    public Optional<User> getById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public User getByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    @Override
    public List<User> getAllByIds(List<Long> ids) {
        return this.userRepository.findAllById(ids);
    }
}
