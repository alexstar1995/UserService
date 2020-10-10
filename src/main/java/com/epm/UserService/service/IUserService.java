package com.epm.UserService.service;

import com.epm.UserService.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<User> createUser(User user);

    Mono<User> updateUser(User user);

    Mono<User> getUserById(Long userId);

    Mono<Void> deleteUser(Long userId);

    Flux<User> getAllUsers();
}