package com.meditourism.meditourism.auth.service;

public interface IJwtService {
    String generateToken(String username);
}
