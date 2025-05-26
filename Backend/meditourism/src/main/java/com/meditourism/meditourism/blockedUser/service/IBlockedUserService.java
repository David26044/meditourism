package com.meditourism.meditourism.blockedUser.service;

import com.meditourism.meditourism.blockedUser.dto.BlockedUserRequestDTO;
import com.meditourism.meditourism.blockedUser.dto.BlockedUserResponseDTO;

import java.util.List;

public interface IBlockedUserService {

    List<BlockedUserResponseDTO> getAllBlockedUsers();
    BlockedUserResponseDTO findBlockedUserByUserId(Long id);
    boolean isBlocked(Long id);
    BlockedUserResponseDTO saveBlockedUser(BlockedUserRequestDTO dto);
    BlockedUserResponseDTO updateBlockedUser(Long id, BlockedUserRequestDTO dto);
    BlockedUserResponseDTO deleteBlockedUser(Long id);


}
