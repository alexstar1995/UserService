package com.epm.UserService.api;

import com.epm.UserService.model.User;
import com.epm.UserService.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return userService.getAllUsers()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("getUser/{userId}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/createUser")
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userService.createUser(user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping
    public Mono<ResponseEntity<User>> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteUser(Long userId) {
        return userService.deleteUser(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}