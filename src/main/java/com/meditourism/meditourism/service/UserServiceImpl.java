package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.UserEntity;
import com.meditourism.meditourism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /*
    * Lanza una excepcion si el correo ya ha sido usado con un usuario
    * encripta la contraseña, se la asigna al objeto y lo guarda en la db
    * */
    @Override
    public UserEntity saveUser(UserEntity user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        //Encripta contraseña
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /*
    * Hay que usar .save, este meetodo buscara el ID, si existe lo actualiza y si no existe lo guarda
    * */
    @Override
    public UserEntity updateUser(UserEntity user) {
        return userRepository;
    }

    @Override
    public void deleteUser(Long id) {

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
