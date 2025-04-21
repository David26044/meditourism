package com.meditourism.meditourism.service;

import com.meditourism.meditourism.dto.AuthenticationRequest;
import com.meditourism.meditourism.entity.UserEntity;
import com.meditourism.meditourism.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int authenticate(AuthenticationRequest request) {
        UserEntity user = userService.getUserByEmail(request.getEmail());

        if (user == null) {
            return 0; // Email no encontrado
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return 2; // Login exitoso
        }

        return 1; // Clave incorrecta
    }
}
