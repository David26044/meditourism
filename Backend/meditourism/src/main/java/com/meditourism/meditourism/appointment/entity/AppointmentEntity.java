package com.meditourism.meditourism.appointment.entity;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id"),
            @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id")
    })
    private ClinicTreatmentEntity clinicTreatment;

    @Column(name = "begin_date", nullable = false)
    private LocalDateTime beginDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW, REJECTED
    }

    // Métodos para Treatment
    public TreatmentEntity getTreatment() {
        return this.clinicTreatment.getTreatment();
    }

    public void setTreatment(TreatmentEntity treatment) {
        if (this.clinicTreatment == null) {
            this.clinicTreatment = new ClinicTreatmentEntity();
        }
        this.clinicTreatment.setTreatment(treatment);
    }

    // Métodos para Clinic
    public ClinicEntity getClinic() {
        return this.clinicTreatment.getClinic();
    }

    public void setClinic(ClinicEntity clinic) {
        if (this.clinicTreatment == null) {
            this.clinicTreatment = new ClinicTreatmentEntity();
        }
        this.clinicTreatment.setClinic(clinic);
    }

    public BigDecimal getPrice() {
        return this.clinicTreatment.getPrice();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ClinicTreatmentEntity getClinicTreatment() {
        return clinicTreatment;
    }

    public void setClinicTreatment(ClinicTreatmentEntity clinicTreatment) {
        this.clinicTreatment = clinicTreatment;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}