package com.meditourism.meditourism.clinic.entity;

import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clinics")
public class ClinicEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(unique = true)
    private String email;

    @Column
    private String address;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String contactInfo) {
        this.email = contactInfo;
    }

    // Relación con ReviewEntity
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    // Relación con ClinicTreatmentEntity
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ClinicTreatmentEntity> clinicTreatments = new ArrayList<>();
}
