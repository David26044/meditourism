package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {
    UserResponseDTO saveUser(UserDTO dto);
    UserResponseDTO updateUser(Long id, UserDTO dto, Authentication authenticate);
    UserResponseDTO deleteUserById(Long id);
    UserResponseDTO getUserResponseDTOById(Long id);
    //void verifyEmail();
    UserEntity getUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
    UserEntity getUserEntityById(Long id);
    List<UserResponseDTO> getAllUsersResponseDTO();
    UserResponseDTO getMyUser(String email);
    void verifyEmail(String email);
}
