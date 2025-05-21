package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.user.entity.UserEntity;

import java.util.List;

public interface IUserService {
    UserEntity saveUser(UserEntity user);
    UserEntity updateUser(UserEntity user);
    UserEntity deleteUserById(Long id);
    UserEntity getUserById(Long id);
    //void verifyEmail();
    UserEntity getUserByEmail(String email);
    UserEntity updatePassword(Long id, String newPassword);
    List<UserEntity> getAllUsers();
}
