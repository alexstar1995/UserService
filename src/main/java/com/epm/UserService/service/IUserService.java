package com.epm.UserService.service;

import com.epm.UserService.model.User;
import com.epm.UserService.model.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<User> createUser(User user);

    Mono<User> updateUser(User user);

    Mono<User> getUserById(Long userId, Long detailsId);

    Mono<Void> deleteUser(Long userId, Long detailsId);

    Flux<User> getAllUsers();

    Mono<UserDetails> getUserDetails(Long userId);
}