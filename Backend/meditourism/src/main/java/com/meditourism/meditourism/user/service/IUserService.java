package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {
    boolean existsByEmail(String email);

    UserResponseDTO saveUser(UserDTO dto);
    UserResponseDTO updateUser(Long id, UserDTO dto, Authentication authenticate);
    UserResponseDTO deleteUserById(Long id, Authentication authenticate);
    UserResponseDTO getUserResponseDTOById(Long id);
    //void verifyEmail();
    UserEntity getUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
    UserEntity getUserEntityById(Long id);
    List<UserResponseDTO> getAllUsersResponseDTO();
    UserResponseDTO getMyUser(String email);
    void verifyEmail(String email);
    UserResponseDTO refreshUserInfo(String email);

    // Admin methods
    UserResponseDTO updateUserRole(Long userId, Long roleId, Authentication authenticate);
    UserResponseDTO adminUpdateUser(Long id, UserDTO dto, Authentication authenticate);
    UserResponseDTO adminDeleteUser(Long id, Authentication authenticate);
    UserResponseDTO deleteNormalUser(Long id, Authentication authenticate);
}
