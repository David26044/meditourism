package com.meditourism.meditourism.blockedUser.controller;

import com.meditourism.meditourism.blockedUser.dto.BlockedUserRequestDTO;
import com.meditourism.meditourism.blockedUser.dto.BlockedUserResponseDTO;
import com.meditourism.meditourism.blockedUser.service.IBlockedUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/blocked-users")
public class BlockedUserController {

    @Autowired
    private IBlockedUserService blockedUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BlockedUserResponseDTO>> getAllBlockedUsers() {
        return ResponseEntity.ok(blockedUserService.getAllBlockedUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> getBlockedUserById(@PathVariable Long id) {
        return ResponseEntity.ok(blockedUserService.findBlockedUserByUserId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BlockedUserResponseDTO> postBlockedUser(@RequestBody @Valid BlockedUserRequestDTO dto) {
        BlockedUserResponseDTO savedBlockedUser = blockedUserService.saveBlockedUser(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedBlockedUser.getId())
                        .toUri())
                .body(savedBlockedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> patchBlockedUser(@PathVariable Long id, @RequestBody BlockedUserRequestDTO dto) {
        return ResponseEntity.ok(blockedUserService.updateBlockedUser(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BlockedUserResponseDTO> deleteBlockedUser(@PathVariable Long id) {
        return ResponseEntity.ok(blockedUserService.deleteBlockedUser(id));
    }
}
