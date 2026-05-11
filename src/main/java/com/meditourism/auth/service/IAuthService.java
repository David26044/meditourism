package com.meditourism.auth.service;

import com.meditourism.auth.dto.AuthRequest;
import com.meditourism.auth.dto.AuthResponse;
import com.meditourism.auth.dto.ChangePasswordDTO;
import com.meditourism.user.dto.UserDTO;
import com.meditourism.user.entity.UserEntity;

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
