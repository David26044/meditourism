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

@Service
public class BlockedUserService implements IBlockedUserService{

    @Autowired
    BlockedUserRepository blockedUserRepository;

    @Autowired
    IUserService userService;


    /**
     * @return 
     */
    @Override
    public List<BlockedUserResponseDTO> getAllBlockedUsers() {
        return BlockedUserResponseDTO.fromEntityList(blockedUserRepository.findAll());
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public BlockedUserResponseDTO findBlockedUserByUserId(Long id) {
        return new BlockedUserResponseDTO(blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " + id)));
    }

    @Override
    public boolean isBlocked(Long id){
        return blockedUserRepository.findById(id).isPresent();
    }

    /**
     * @param dto 
     * @return
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
     * @param id 
     * @param dto
     * @return
     */
    @Override
    public BlockedUserResponseDTO updateBlockedUser(Long id, BlockedUserRequestDTO dto) {
        BlockedUserEntity blockedUser = blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " +id));
        if (dto.getReason() != null){
            blockedUser.setReason(dto.getReason());
        }
        return new BlockedUserResponseDTO(blockedUserRepository.save(blockedUser));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public BlockedUserResponseDTO deleteBlockedUser(Long id) {
        BlockedUserEntity blockedUser = blockedUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró al usuario bloqueado con ID: " +id));
        blockedUserRepository.delete(blockedUser);
        return new BlockedUserResponseDTO(blockedUser);
    }
}
