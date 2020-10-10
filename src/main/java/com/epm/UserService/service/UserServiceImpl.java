package com.epm.UserService.service;

import com.epm.UserService.model.User;
import com.epm.UserService.model.UserDetails;
import com.epm.UserService.repository.UserDetailsRepository;
import com.epm.UserService.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Date;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public Mono<User> getUserById(Long userId) {
        return userRepository.findById(userId)
                .flatMap(usr -> userDetailsRepository
                            .findById(usr.getUserDetails().getDetailId())
                            .doOnSuccess(usr::setUserDetails)
                            .then(Mono.just(usr))
        );
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .flatMap(usr -> userDetailsRepository
                        .findById(usr.getUserDetails().getDetailId())
                        .doOnSuccess(usr::setUserDetails)
                        .thenMany(Flux.just(usr)));
    }

    @Override
    @Transactional
    public Mono<User> createUser(User user) {

        user.setCreatedDate(new Date(System.currentTimeMillis()));

        return userRepository.save(user)
                .flatMap(usr -> {
                    user.setUserId(usr.getUserId());
                    user.getUserDetails().setUserId(usr.getUserId());
                    return userDetailsRepository.save(usr.getUserDetails())
                            .then(Mono.just(user));
                });
    }

    @Override
    @Transactional
    public Mono<User> updateUser(User user) {

        Mono<UserDetails> newUserDetails = getNewUserDetails(user.getUserDetails());
        Mono<User> newUser = getNewUser(user);

        return Mono.zip(newUser, newUserDetails, (usr, details) -> {
            usr.setUserDetails(details);
            return usr;
        });
    }

    @Override
    @Transactional
    public Mono<Void> deleteUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> userDetailsRepository
                             .deleteById(user.getUserDetails().getDetailId())
                             .doOnSuccess(details -> userRepository.deleteById(userId))
                );
    }

    private Mono<User> getNewUser(User user) {
        return userRepository.findById(user.getUserId())
                .flatMap(oldUser -> {
                    setUser(oldUser, user);
                    return userRepository.save(oldUser);
                });
    }

    private Mono<UserDetails> getNewUserDetails(UserDetails userDetails) {
        return userDetailsRepository.findById(userDetails.getDetailId())
                .flatMap(oldDetails -> {
                    setUserDetails(oldDetails, userDetails);
                    return userDetailsRepository.save(oldDetails);
                });
    }

    private void setUser(User oldUser, User newUser) {
        oldUser.setUsername(newUser.getUsername());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setUpdatedDate(new Date(System.currentTimeMillis()));
    }

    private void setUserDetails(UserDetails oldDetails, UserDetails newDetails) {
        oldDetails.setAge(newDetails.getAge());
        oldDetails.setFirstName(newDetails.getFirstName());
        oldDetails.setLastName(newDetails.getLastName());
        oldDetails.setGender(newDetails.getGender());
    }
}