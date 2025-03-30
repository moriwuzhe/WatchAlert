package com.watchalert.service;

public interface JwtService {
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
} 