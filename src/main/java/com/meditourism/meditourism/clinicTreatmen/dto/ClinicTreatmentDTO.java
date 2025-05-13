package com.meditourism.meditourism.clinicTreatmen.dto;

import java.math.BigDecimal;

public class ClinicTreatmentDTO {

    private Long clinicId;
    private Long treatmentId;
    private BigDecimal price;

    public ClinicTreatmentDTO(Long clinicId, Long treatmentId, BigDecimal price) {
        this.clinicId = clinicId;
        this.treatmentId = treatmentId;
        this.price = price;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
