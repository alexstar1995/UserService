package com.epm.UserService.service;

import com.epm.UserService.model.User;
import com.epm.UserService.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> createUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedDate(now);
        user.setUpdatedDate(now);
        return userRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(User user) {
        return userRepository.findById(user.getUserId())
                .flatMap(oldUser -> {
                    setUser(oldUser, user);
                    return userRepository.save(oldUser);
                });
    }

    @Override
    public Mono<User> deleteUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> userRepository.deleteById(userId).then(Mono.just(user)));
    }

    private void setUser(User oldUser, User newUser) {
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setAge(newUser.getAge());
        oldUser.setGender(newUser.getGender());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setUpdatedDate(LocalDateTime.now());
    }
}