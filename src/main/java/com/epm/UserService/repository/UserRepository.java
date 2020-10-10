package com.epm.UserService.repository;

import com.epm.UserService.model.User;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}