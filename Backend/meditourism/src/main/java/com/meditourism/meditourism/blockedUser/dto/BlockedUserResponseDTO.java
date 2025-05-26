package com.meditourism.meditourism.blockedUser.dto;

import com.meditourism.meditourism.blockedUser.entity.BlockedUserEntity;
import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.user.dto.UserResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockedUserResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private String reason;
    private LocalDateTime date;


    public BlockedUserResponseDTO(Long id, UserResponseDTO user, String reason, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.reason = reason;
        this.date = date;
    }

    public BlockedUserResponseDTO(){}

    public BlockedUserResponseDTO(BlockedUserEntity entity){
        this.id = entity.getId();
        this.user = new UserResponseDTO(entity.getBlockedUser());
        this.date = entity.getDate();
        this.reason = entity.getReason();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static List<BlockedUserResponseDTO> fromEntityList(List<BlockedUserEntity> entities) {
        List<BlockedUserResponseDTO> dtoList = new ArrayList<>();
        for (BlockedUserEntity entity : entities) {
            dtoList.add(new BlockedUserResponseDTO(entity));
        }
        return dtoList;
    }

}
