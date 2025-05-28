package com.meditourism.meditourism.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class ScheduleRequestDTO {

    @NotNull(message = "El de la clinica no puede ser nulo")
    private Long clinicId;
    @NotNull(message = "EL dia de la semana no puede ser nulo")
    private Integer dayOfWeek;
    @NotNull(message = "La hora de inicio no puede ser nula")
    private LocalTime startTime;
    @NotNull(message = "La hora de fin no puede ser nula")
    private LocalTime endTime;

    public ScheduleRequestDTO(Long clinicId, Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.clinicId = clinicId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleRequestDTO() {}

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
