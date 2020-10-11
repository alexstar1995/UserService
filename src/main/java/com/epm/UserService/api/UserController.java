package com.epm.UserService.api;

import com.epm.UserService.model.User;
import com.epm.UserService.service.IUserService;

import com.epm.UserService.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AppConstants.BASE_RESOURCE_URL)
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
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

    @PutMapping("/updateUser/{userId}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long userId, @RequestBody User user) {
        user.setUserId(userId);
        return userService.updateUser(user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/deleteUser/{userId}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
                .map(user -> ResponseEntity.ok("User deleted"))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}