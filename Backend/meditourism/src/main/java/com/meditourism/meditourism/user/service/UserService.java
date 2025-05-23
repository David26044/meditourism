package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.role.service.IRoleService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsersResponseDTO() {
        List<UserEntity> savedUsers = userRepository.findAll();
        List<UserResponseDTO> responseUsers = new ArrayList<>();
        for(UserEntity savedUser : savedUsers){
            responseUsers.add(new UserResponseDTO(savedUser));
        }
        return responseUsers;
    }

    @Override
    public UserResponseDTO getUserResponseDTOById(Long id) {
        return new UserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id)));
    }

    public UserEntity getUserEntityById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

    }

    /*
    * Lanza una excepcion si el correo ya ha sido usado con un usuario
    * encripta la contraseña, se la asigna al objeto y lo guarda en la db
    * */
//    @Override
//    public UserResponseDTO saveUser(UserDTO dto) {
//        // Verifica si el email ya está en uso
//        if (userRepository.existsByEmail(dto.getEmail())) {
//            throw new ResourceAlreadyExistsException("El correo ya está registrado: " + dto.getEmail());
//        }
//
//        //setteo el nuevo usuario.
//        UserEntity user = new UserEntity();
//
//        user.setEmail(dto.getEmail());
//        user.setName(dto.getName());
//        user.setVerified(false);
//
//        RoleEntity role = roleService.getRoleById(dto.getRoleId());
//
//        user.setRoleEntity(role); // Asigna la referencia al rol
//
//        // Encripta la contraseña
//        String hashedPassword = passwordEncoder.encode(dto.getPassword());
//        user.setPassword(hashedPassword);
//
//        // Guarda y retorna
//        UserEntity savedUser = userRepository.save(user);
//
//        return new UserResponseDTO(savedUser);
//    }

    /**
     * @param dto 
     * @return
     */
    @Override
    public UserResponseDTO saveUser(UserDTO dto) {
        return null;
    }

    /*
    * Hay que usar .save, este meetodo buscara el ID, si existe lo actualiza y si no existe lo guarda
    * */
    @Override
    public UserResponseDTO updateUser(Long id, UserDTO dto) {
        UserEntity updateUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con ID: " + id));
        if(dto.getEmail() != null){
            updateUser.setEmail(dto.getEmail());
        }
        if(dto.getName() != null){
            updateUser.setName(dto.getName());
        }
        userRepository.save(updateUser);
        return new UserResponseDTO(updateUser);
    }

    @Override
    public UserResponseDTO deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con ID: " + id));
        userRepository.deleteById(id);
        return new UserResponseDTO(user);
    }

    @Override
    public UserEntity updatePassword(Long id, String newPassword) {
        return null;
    }
}