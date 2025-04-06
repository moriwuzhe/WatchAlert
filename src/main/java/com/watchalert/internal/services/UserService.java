package com.watchalert.internal.services;

import com.watchalert.internal.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void initialize();
    User findByUsername(String username);
    User createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    boolean validatePassword(User user, String password);
} 