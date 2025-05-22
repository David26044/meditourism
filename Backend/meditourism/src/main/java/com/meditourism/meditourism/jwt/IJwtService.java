package com.meditourism.meditourism.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateToken(String email);

    String getToken(UserDetails user);
}
