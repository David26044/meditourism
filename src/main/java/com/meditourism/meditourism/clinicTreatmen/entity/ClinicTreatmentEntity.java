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

    // getters y setters
}

