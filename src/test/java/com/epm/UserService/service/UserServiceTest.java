package com.epm.UserService.service;

import com.epm.UserService.model.User;
import com.epm.UserService.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Flux<User> ALL_USERS = Flux.just(new User());
    private static final Mono<User> USER = Mono.just(new User());
    private static final Long USER_ID = 1L;
    private static final User NEW_USER = new User();

    @Test
    public void getAllUsers() {

        when(userRepository.findAll()).thenReturn(ALL_USERS);
        Flux<User> result = userService.getAllUsers();

        verify(userRepository).findAll();

        assertEquals(result, ALL_USERS);
    }

    @Test
    public void getUserById() {

        when(userRepository.findById(USER_ID)).thenReturn(USER);

        Mono<User> result = userService.getUserById(USER_ID);

        verify(userRepository).findById(USER_ID);

        assertEquals(result, USER);
    }

    @Test
    public void createUser() {

        when(userRepository.save(NEW_USER)).thenReturn(USER);

        Mono<User> result = userService.createUser(NEW_USER);

        verify(userRepository).save(NEW_USER);

        assertEquals(result, USER);
    }
}