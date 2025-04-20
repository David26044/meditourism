package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.UserEntity;
import com.meditourism.meditourism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(UserEntity user) {
        // Evita duplicados si quieres:
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        // Encriptar la contraseña
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public UserEntity getUserById(Long id) {
        return null;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserEntity updatePassword(Long id, String newPassword) {
        return null;
    }
}
