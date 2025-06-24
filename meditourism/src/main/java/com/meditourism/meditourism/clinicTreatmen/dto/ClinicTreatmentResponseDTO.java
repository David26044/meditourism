package com.meditourism.meditourism.clinicTreatmen.dto;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClinicTreatmentResponseDTO {

    private ClinicTreatmentEntityPK pk;
    private ClinicDTO clinicDTO;
    private TreatmentDTO treatmentDTO;
    private BigDecimal price;

    public ClinicTreatmentResponseDTO() {}

    public ClinicTreatmentResponseDTO(ClinicTreatmentEntityPK pk, ClinicDTO clinicDTO, TreatmentDTO treatmentDTO, BigDecimal price) {
        this.pk = pk;
        this.clinicDTO = clinicDTO;
        this.treatmentDTO = treatmentDTO;
        this.price = price;
    }

    public ClinicTreatmentResponseDTO(ClinicTreatmentEntity entity) {
        this.pk = entity.getId();
        this.price = entity.getPrice();
        this.clinicDTO = new ClinicDTO(entity.getClinic());
        this.treatmentDTO = new TreatmentDTO(entity.getTreatment());
    }

    public ClinicTreatmentEntityPK getPk() {
        return pk;
    }

    public void setPk(ClinicTreatmentEntityPK pk) {
        this.pk = pk;
    }

    public ClinicDTO getClinicDTO() {
        return clinicDTO;
    }

    public void setClinicDTO(ClinicDTO clinicDTO) {
        this.clinicDTO = clinicDTO;
    }

    public TreatmentDTO getTreatmentDTO() {
        return treatmentDTO;
    }

    public void setTreatmentDTO(TreatmentDTO treatmentDTO) {
        this.treatmentDTO = treatmentDTO;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static List<ClinicTreatmentResponseDTO> fromEntityList(List<ClinicTreatmentEntity> entityList) {
        List<ClinicTreatmentResponseDTO> dtoList = new ArrayList<>();
        for (ClinicTreatmentEntity entity : entityList) {
            dtoList.add(new ClinicTreatmentResponseDTO(entity));
        }
        return dtoList;
    }
}
