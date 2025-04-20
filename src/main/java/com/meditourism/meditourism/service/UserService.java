package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    UserEntity updateUser(UserEntity user);
    void deleteUser(Long id);
    UserEntity findUserById(Long id);
    //void verifyEmail();
    UserEntity findUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
    List<UserEntity> findAllUsers();
}
