package com.meditourism.meditourism.appointment.dto;

import com.meditourism.meditourism.appointment.entity.AppointmentEntity;
import com.meditourism.meditourism.appointment.entity.AppointmentEntity.AppointmentStatus;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentResponseDTO {
    private Long id;
    private UserEntity user;
    private TreatmentEntity treatment;
    private ClinicEntity clinic;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;


    public AppointmentResponseDTO(Long id, UserEntity user, TreatmentEntity treatment, ClinicEntity clinic,
                                  LocalDateTime beginDate, LocalDateTime endDate,
                                  AppointmentStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.treatment = treatment;
        this.clinic = clinic;
        this.beginDate= beginDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(AppointmentEntity entity) {
        this.id = entity.getId();
        this.user = entity.getUser();
        this.treatment = entity.getTreatment();
        this.clinic = entity.getClinic();
        this.beginDate = entity.getBeginDate();
        this.endDate = entity.getEndDate();
        this.status = entity.getStatus();
        this.notes = entity.getNotes();
        this.createdAt = entity.getCreatedAt();
    }

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

    public TreatmentEntity getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentEntity treatment) {
        this.treatment = treatment;
    }

    public ClinicEntity getClinic() {
        return clinic;
    }

    public void setClinic(ClinicEntity clinic) {
        this.clinic = clinic;
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

    public static List<AppointmentResponseDTO> fromEntityList(List<AppointmentEntity> entityList) {
        List<AppointmentResponseDTO> dtoList = new ArrayList<>();
        for (AppointmentEntity entity : entityList) {
            dtoList.add(new AppointmentResponseDTO (entity));
        }
        return dtoList;
    }


}