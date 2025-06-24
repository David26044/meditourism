package com.meditourism.meditourism.appointment.dto;



import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {

    @NotNull(message = "El id de tratamiento no puede estar vacío")
    private Long treatmentId;
    @NotNull(message = "El id de la clinica no puede ser nulo")
    private Long clinicId;

    @NotNull(message = "Debes especificar una fecha de fin")
    private LocalDateTime beginDate;
    @NotNull(message = "Debes especificar una fecha de inicio")
    private LocalDateTime endDate;
    private String notes;

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}