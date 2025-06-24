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

/**
 * Servicio para manejar las operaciones relacionadas con usuarios
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Obtiene todos los usuarios en formato DTO
     * @return Lista de UserResponseDTO con todos los usuarios
     */
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
     * Obtiene la información del usuario autenticado
     * @param email Correo electrónico del usuario
     * @return UserResponseDTO con la información del usuario
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserResponseDTO getMyUser(String email) {
        return new UserResponseDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario con email: " + email)));
    }

    /**
     * Verifica el correo electrónico de un usuario
     * @param email Correo electrónico a verificar
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public void verifyEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo verificar el correo: " + email));
        user.setVerified(true);
        userRepository.save(user);
    }

    /**
     * Obtiene un usuario por su ID en formato DTO
     * @param id ID del usuario
     * @return UserResponseDTO con la información del usuario
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserResponseDTO getUserResponseDTOById(Long id) {
        return new UserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id)));
    }

    /**
     * Obtiene la entidad de usuario por su ID
     * @param id ID del usuario
     * @return Entidad UserEntity
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    public UserEntity getUserEntityById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Obtiene un usuario por su correo electrónico
     * @param email Correo electrónico del usuario
     * @return Entidad UserEntity
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    /**
     * Verifica si existe un usuario con el correo electrónico especificado
     * @param email Correo electrónico a verificar
     * @return true si existe, false si no
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Guarda un nuevo usuario (no implementado)
     * @param dto DTO con la información del usuario
     * @return UserResponseDTO con el usuario guardado
     */
    @Override
    public UserResponseDTO saveUser(UserDTO dto) {
        return null;
    }

    /**
     * Actualiza la información de un usuario
     * @param id ID del usuario a actualizar
     * @param dto DTO con la nueva información
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario actualizado
     * @throws UnauthorizedAccessException si el usuario no tiene permisos
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
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

    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario eliminado
     * @throws UnauthorizedAccessException si el usuario no tiene permisos
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserResponseDTO deleteUserById(Long id, Authentication authenticate) {
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());

        boolean isOwner = authenticatedUser.getId().equals(id);

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();

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

    /**
     * Actualiza la contraseña de un usuario (no implementado)
     * @param id ID del usuario
     * @param newPassword Nueva contraseña
     * @return Entidad UserEntity actualizada
     */
    @Override
    public UserEntity updatePassword(Long id, String newPassword) {
        return null;
    }

    /**
     * Actualiza la información del usuario en la respuesta
     * @param email Correo electrónico del usuario
     * @return UserResponseDTO con la información actualizada
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    public UserResponseDTO refreshUserInfo(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return new UserResponseDTO(user);
    }

    // Métodos de administración

    /**
     * Actualiza el rol de un usuario (solo para administradores)
     * @param userId ID del usuario a actualizar
     * @param roleId ID del nuevo rol
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario actualizado
     * @throws UnauthorizedAccessException si no es administrador
     * @throws ResourceNotFoundException si no se encuentra usuario o rol
     */
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

    /**
     * Actualiza un usuario como administrador
     * @param id ID del usuario a actualizar
     * @param dto DTO con la nueva información
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario actualizado
     * @throws UnauthorizedAccessException si no es administrador
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
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

    /**
     * Elimina un usuario como administrador
     * @param id ID del usuario a eliminar
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario eliminado
     * @throws UnauthorizedAccessException si no es administrador o intenta eliminarse a sí mismo
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
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

    /**
     * Elimina un usuario normal (solo puede eliminarse a sí mismo)
     * @param id ID del usuario a eliminar
     * @param authenticate Objeto de autenticación para verificar permisos
     * @return UserResponseDTO con el usuario eliminado
     * @throws UnauthorizedAccessException si intenta eliminar otro usuario
     * @throws ResourceNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserResponseDTO deleteNormalUser(Long id, Authentication authenticate) {
        UserEntity authenticatedUser = ((UserEntity) authenticate.getPrincipal());

        if (!authenticatedUser.getId().equals(id)) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar este usuario");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con ID: " + id));

        userRepository.deleteById(id);
        return new UserResponseDTO(user);
    }
}