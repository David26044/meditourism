package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;

import java.util.List;

public interface IUserService {
    UserResponseDTO saveUser(UserDTO dto);
    UserResponseDTO updateUser(Long id, UserDTO dto);
    UserResponseDTO deleteUserById(Long id);
    UserResponseDTO getUserResponseDTOById(Long id);
    //void verifyEmail();
    UserEntity getUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
    UserEntity getUserEntityById(Long id);
    List<UserResponseDTO> getAllUsersResponseDTO();
}
