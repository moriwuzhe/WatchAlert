package com.watchalert.service.impl;

import com.watchalert.entity.User;
import com.watchalert.repository.UserRepository;
import com.watchalert.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            log.info("创建新用户: {}", user.getUsername());
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("用户名已存在: " + user.getUsername());
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            log.info("更新用户: {}", user.getId());
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("用户不存在: " + user.getId()));
            
            if (!existingUser.getUsername().equals(user.getUsername()) && 
                userRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("用户名已存在: " + user.getUsername());
            }
            
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser.getPassword());
            }
        }
        
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
} 