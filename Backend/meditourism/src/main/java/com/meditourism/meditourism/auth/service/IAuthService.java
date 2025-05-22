package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.user.dto.UserDTO;

public interface IAuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(UserDTO request);
}
