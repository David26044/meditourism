package com.meditourism.meditourism.doctor.entity;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.schedule.ScheduleEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctors")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private ClinicEntity clinic;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedules;

    @ManyToMany
    @JoinTable(
            name = "doctor_treatment",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "treatment_id"))
    private List<TreatmentEntity> treatments;
}