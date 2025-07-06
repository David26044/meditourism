package com.meditourism.meditourism.treatment.entity;

import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="treatments")
public class TreatmentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

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

    // Relación con ClinicTreatmentEntity (borrado en cascada)
    @OneToMany(mappedBy = "treatment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ClinicTreatmentEntity> clinicTreatments = new ArrayList<>();

    // Relación con ContactFormEntity (borrado en cascada)
    @OneToMany(mappedBy = "treatment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ContactFormEntity> contactForms = new ArrayList<>();

}