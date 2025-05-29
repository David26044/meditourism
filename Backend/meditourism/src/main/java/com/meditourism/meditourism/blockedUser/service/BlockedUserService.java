package com.meditourism.meditourism.blockedUser.service;

import com.meditourism.meditourism.blockedUser.dto.BlockedUserRequestDTO;
import com.meditourism.meditourism.blockedUser.dto.BlockedUserResponseDTO;
import com.meditourism.meditourism.blockedUser.entity.BlockedUserEntity;
import com.meditourism.meditourism.blockedUser.repository.BlockedUserRepository;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar usuarios bloqueados en el sistema.
 */
@Service
public class BlockedUserService implements IBlockedUserService {

    @Autowired
    BlockedUserRepository blockedUserRepository;

    @Autowired
    IUserService userService;

    /**
     * Obtiene todos los usuarios bloqueados en el sistema.
     *
     * @return Lista de DTOs con la información de todos los usuarios bloqueados
     */
    @Override
    public List<BlockedUserResponseDTO> getAllBlockedUsers() {
        return BlockedUserResponseDTO.fromEntityList(blockedUserRepository.findAll());
    }

    /**
     * Busca un usuario bloqueado por su ID.
     *
     * @param id ID del usuario bloqueado a buscar
     * @return DTO con la información del usuario bloqueado
     * @throws ResourceNotFoundException Si no se encuentra el usuario bloqueado con el ID especificado
     */
    @Override
    public BlockedUserResponseDTO findBlockedUserByUserId(Long id) {
        return new BlockedUserResponseDTO(blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " + id)));
    }

    /**
     * Verifica si un usuario está bloqueado.
     *
     * @param id ID del usuario a verificar
     * @return true si el usuario está bloqueado, false en caso contrario
     */
    @Override
    public boolean isBlocked(Long id) {
        return blockedUserRepository.findById(id).isPresent();
    }

    /**
     * Guarda un nuevo usuario bloqueado en el sistema.
     *
     * @param dto DTO con la información del usuario a bloquear
     * @return DTO con la información del usuario bloqueado guardado
     * @throws ResourceAlreadyExistsException Si el usuario ya está bloqueado
     */
    @Override
    public BlockedUserResponseDTO saveBlockedUser(BlockedUserRequestDTO dto) {
        if (blockedUserRepository.existsById(dto.getUserId())) {
            throw new ResourceAlreadyExistsException("El usuario con ID: " + dto.getUserId() + " ya está bloqueado");
        }

        BlockedUserEntity blockedUser = new BlockedUserEntity();
        UserEntity user = userService.getUserEntityById(dto.getUserId());
        blockedUser.setBlockedUser(user);
        blockedUser.setReason(dto.getReason());
        return new BlockedUserResponseDTO(blockedUserRepository.save(blockedUser));
    }

    /**
     * Actualiza la información de un usuario bloqueado.
     *
     * @param id ID del usuario bloqueado a actualizar
     * @param dto DTO con la nueva información del usuario bloqueado
     * @return DTO con la información actualizada del usuario bloqueado
     * @throws ResourceNotFoundException Si no se encuentra el usuario bloqueado con el ID especificado
     */
    @Override
    public BlockedUserResponseDTO updateBlockedUser(Long id, BlockedUserRequestDTO dto) {
        BlockedUserEntity blockedUser = blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " + id));
        if (dto.getReason() != null) {
            blockedUser.setReason(dto.getReason());
        }
        return new BlockedUserResponseDTO(blockedUserRepository.save(blockedUser));
    }

    /**
     * Elimina un usuario bloqueado del sistema.
     *
     * @param id ID del usuario bloqueado a eliminar
     * @return DTO con la información del usuario bloqueado eliminado
     * @throws ResourceNotFoundException Si no se encuentra el usuario bloqueado con el ID especificado
     */
    @Override
    public BlockedUserResponseDTO deleteBlockedUser(Long id) {
        BlockedUserEntity blockedUser = blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " + id));
        blockedUserRepository.delete(blockedUser);
        return new BlockedUserResponseDTO(blockedUser);
    }
}