package com.meditourism.meditourism.clinicTreatmen.entity;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class ClinicTreatmentEntityPK implements Serializable {

    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "treatment_id")
    private Long treatmentId;

    public ClinicTreatmentEntityPK(Long clinicId, Long treatmentId) {
        this.clinicId = clinicId;
        this.treatmentId = treatmentId;
    }

    public ClinicTreatmentEntityPK() {
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

}

