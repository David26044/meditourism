package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * @param email 
     * @return
     */
    @Override
    public UserResponseDTO getMyUser(String email) {
        return new UserResponseDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario con email: " + email)));
    }

    /**
     * @param email
     */
    @Override
    public void verifyEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo verificar el correo: " + email));
        user.setVerified(true);
        userRepository.save(user);

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
    public UserResponseDTO updateUser(Long id, UserDTO dto, Authentication authenticate) {
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());

        // Verificar permisos
        if (!authenticatedUser.getId().equals(id)) {
            throw new UnauthorizedAccessException("No tienes permiso para editar este usuario");
        }

        // Obtener el usuario a actualizar (el mismo que el autenticado en este caso)
        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Actualizar campos si vienen en el DTO
        if(dto.getEmail() != null){
            userToUpdate.setEmail(dto.getEmail());
        }
        if(dto.getName() != null){
            userToUpdate.setName(dto.getName());
        }

        // Guardar cambios
        UserEntity updatedUser = userRepository.save(userToUpdate);
        return new UserResponseDTO(updatedUser);
    }

    @Override
    public UserResponseDTO deleteUserById(Long id, Authentication authenticate) {
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());

        boolean isOwner = authenticatedUser.getId().equals(id);

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities(); // ← aquí

        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar este usuario");
        }

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