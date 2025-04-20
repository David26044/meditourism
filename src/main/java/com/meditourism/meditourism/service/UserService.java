package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity user);
    UserEntity updateUser(UserEntity user);
    void deleteUser(Long id);
    UserEntity getUserById(Long id);
    //void verifyEmail();
    UserEntity getUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
}
