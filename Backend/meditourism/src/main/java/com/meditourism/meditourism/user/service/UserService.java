package com.meditourism.meditourism.user.service;

import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.repository.UserRepository;
import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.role.repository.RoleRepository;
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
    
    @Autowired
    private RoleRepository roleRepository;

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

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

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

    public UserResponseDTO refreshUserInfo(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return new UserResponseDTO(user);
    }

    // New admin methods
    public UserResponseDTO updateUserRole(Long userId, Long roleId, Authentication authenticate) {
        // Verify admin permissions
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());
        boolean isAdmin = authenticate.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            throw new UnauthorizedAccessException("Solo los administradores pueden cambiar roles");
        }

        // Find user to update
        UserEntity userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Find role
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + roleId));

        // Update role - assuming UserEntity has a role field with setter
        userToUpdate.setRoleEntity(role);
        UserEntity updatedUser = userRepository.save(userToUpdate);
        
        return new UserResponseDTO(updatedUser);
    }

    public UserResponseDTO adminUpdateUser(Long id, UserDTO dto, Authentication authenticate) {
        // Verify admin permissions
        boolean isAdmin = authenticate.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            throw new UnauthorizedAccessException("Solo los administradores pueden editar usuarios");
        }

        UserEntity userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Update fields if provided
        if (dto.getEmail() != null) {
            userToUpdate.setEmail(dto.getEmail());
            userToUpdate.setVerified(false); // Reset verification if email changes
        }
        if (dto.getName() != null) {
            userToUpdate.setName(dto.getName());
        }

        UserEntity updatedUser = userRepository.save(userToUpdate);
        return new UserResponseDTO(updatedUser);
    }

    public UserResponseDTO adminDeleteUser(Long id, Authentication authenticate) {
        // Verify admin permissions
        boolean isAdmin = authenticate.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            throw new UnauthorizedAccessException("Solo los administradores pueden eliminar usuarios");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con ID: " + id));

        // Prevent admin from deleting themselves
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());
        if (authenticatedUser.getId().equals(id)) {
            throw new UnauthorizedAccessException("No puedes eliminar tu propia cuenta desde el panel de administración");
        }

        userRepository.deleteById(id);
        return new UserResponseDTO(user);
    }
}