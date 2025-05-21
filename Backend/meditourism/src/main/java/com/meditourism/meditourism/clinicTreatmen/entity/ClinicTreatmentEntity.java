package com.meditourism.meditourism.clinicTreatmen.entity;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "clinic_treatment")
public class ClinicTreatmentEntity implements Serializable {

    @EmbeddedId
    private ClinicTreatmentEntityPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clinicId")  // nombre del campo en ClinicTreatmentId
    @JoinColumn(name = "clinic_id")
    private ClinicEntity clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentId")
    @JoinColumn(name = "treatment_id")
    private TreatmentEntity treatment;

    @Column
    private BigDecimal price;

    public ClinicTreatmentEntityPK getId() {
        return id;
    }

    public void setId(ClinicTreatmentEntityPK id) {
        this.id = id;
    }

    public ClinicEntity getClinic() {
        return clinic;
    }

    public void setClinic(ClinicEntity clinic) {
        this.clinic = clinic;
    }

    public TreatmentEntity getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentEntity treatment) {
        this.treatment = treatment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

