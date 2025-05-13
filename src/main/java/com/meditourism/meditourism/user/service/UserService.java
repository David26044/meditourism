package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements IUserService {

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
        // Verifica si el email ya está en uso
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo ya está registrado: " + user.getEmail());
        }
        // Encripta la contraseña
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // Guarda y retorna
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }
    /*
    * Hay que usar .save, este meetodo buscara el ID, si existe lo actualiza y si no existe lo guarda
    * */
    @Override
    public UserEntity updateUser(UserEntity user) {
        if(!userRepository.existsById(user.getId())) {
            new ResourceNotFoundException("Usuario no encontrado con ID: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return user;
    }

    @Override
    public UserEntity updatePassword(Long id, String newPassword) {
        return null;
    }
}
