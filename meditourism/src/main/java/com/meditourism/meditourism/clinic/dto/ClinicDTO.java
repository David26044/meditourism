package com.meditourism.meditourism.clinic.dto;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ClinicDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank
    private String description;

    @NotBlank(message = "La información de contacto no puede estar vacía")
    private String contactInfo;

    @NotBlank(message = "La direccion no puede estar vacía")
    private String address;

    public ClinicDTO() {
    }

    public ClinicDTO(Long id, String name, String description, String contactInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contactInfo = contactInfo;
    }

    public ClinicDTO(ClinicEntity clinicEntity){
        this.id = clinicEntity.getId();
        this.name = clinicEntity.getName();
        this.description = clinicEntity.getDescription();
        this.contactInfo = clinicEntity.getContactInfo();
        this.address = clinicEntity.getAddress();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public static List<ClinicDTO> fromEntityList(List<ClinicEntity> entities) {
        List<ClinicDTO> dtoList = new ArrayList<>();
        for (ClinicEntity entity : entities) {
            dtoList.add(new ClinicDTO(entity));
        }
        return dtoList;
    }
}
