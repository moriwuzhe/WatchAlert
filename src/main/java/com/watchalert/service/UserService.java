package com.watchalert.service;

import com.watchalert.entity.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
} 