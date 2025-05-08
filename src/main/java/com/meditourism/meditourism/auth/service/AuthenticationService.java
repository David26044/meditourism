package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthenticationRequest;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private IUserService userService;

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
