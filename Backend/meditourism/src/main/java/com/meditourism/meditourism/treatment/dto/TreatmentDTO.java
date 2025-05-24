package com.meditourism.meditourism.treatment.dto;

import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class TreatmentDTO {

    private Long id;

    @NotBlank(message = "El nombre del tratamiento no puede estar vacío")
    @Size(max = 100, message = "El nombre del tratamiento no puede tener más de 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    private String description;

    public TreatmentDTO() {
    }

    public TreatmentDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public TreatmentDTO(TreatmentEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
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

    public static List<TreatmentDTO> fromEntityList(List<TreatmentEntity> entityList) {
        List<TreatmentDTO> dtoList = new ArrayList<>();
        for (TreatmentEntity entity : entityList) {
            dtoList.add(new TreatmentDTO(entity));
        }
        return dtoList;
    }
}