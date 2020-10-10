package com.epm.UserService.repository;

import com.epm.UserService.model.UserDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserDetailsRepository extends ReactiveCrudRepository<UserDetails, Long> {
}