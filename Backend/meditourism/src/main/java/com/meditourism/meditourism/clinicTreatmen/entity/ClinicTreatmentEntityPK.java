package com.meditourism.meditourism.clinicTreatmen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ClinicTreatmentEntityPK implements Serializable {

    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "treatment_id")
    private Long treatmentId;

    public ClinicTreatmentEntityPK() {
    }

    public ClinicTreatmentEntityPK(Long clinicId, Long treatmentId) {
        this.clinicId = clinicId;
        this.treatmentId = treatmentId;
    }

    // Getters y Setters
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

    // Implementación de equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicTreatmentEntityPK that = (ClinicTreatmentEntityPK) o;
        return Objects.equals(clinicId, that.clinicId) &&
                Objects.equals(treatmentId, that.treatmentId);
    }

    // Implementación de hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(clinicId, treatmentId);
    }

    // Opcional: toString() para mejor legibilidad en logs
    @Override
    public String toString() {
        return "ClinicTreatmentEntityPK{" +
                "clinicId=" + clinicId +
                ", treatmentId=" + treatmentId +
                '}';
    }
}
