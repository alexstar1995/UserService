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
    public Mono<User> getUserById(Long userId, Long detailsId) {

        Mono<User> user = userRepository.findById(userId);
        Mono<UserDetails> userDetails = userDetailsRepository.findById(detailsId);

        return Mono.zip(user, userDetails, (usr, details) -> {
           usr.setUserDetails(details);
           return usr;
        });
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
    public Mono<UserDetails> getUserDetails(Long userId) {
        return userDetailsRepository.findById(userId);
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

        UserDetails userDetails = user.getUserDetails();

        Mono<UserDetails> newUserDetails = userDetailsRepository.findById(userDetails.getDetailId())
                .flatMap(oldDetails -> {
                    setUserDetails(oldDetails, userDetails);
                    return userDetailsRepository.save(oldDetails);
                });

        Mono<User> newUser = userRepository.findById(user.getUserId())
                .flatMap(oldUser -> {
                    setUser(oldUser, user);
                    return userRepository.save(oldUser);
                });

        return Mono.zip(newUser, newUserDetails, (usr, details) -> {
            usr.setUserDetails(details);
            return usr;
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

    @Override
    @Transactional
    public Mono<Void> deleteUser(Long userId, Long detailsId) {
        return userDetailsRepository
                .deleteById(detailsId)
                .doOnSuccess(x -> userRepository.deleteById(userId));
    }
}