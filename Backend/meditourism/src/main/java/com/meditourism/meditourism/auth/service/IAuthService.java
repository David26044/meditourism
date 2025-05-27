package com.meditourism.meditourism.auth.service;

import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.ChangePasswordDTO;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;

public interface IAuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(UserDTO request);

    void sendEmailChangePassword(String email);

    void changePassword(String token, ChangePasswordDTO dto);

    void sendEmailVerification(String email);

    void verifyEmail(String token);

    UserEntity getAuthenticatedUser();

    boolean isAdmin();

    boolean isOwner(Long ownerId);
}
