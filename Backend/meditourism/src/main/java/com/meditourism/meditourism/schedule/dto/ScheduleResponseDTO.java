package com.meditourism.meditourism.schedule.dto;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.schedule.entity.ScheduleEntity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleResponseDTO {

    private Long id;
    private ClinicDTO clinic;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleResponseDTO(Long id, ClinicDTO clinic, Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.clinic = clinic;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleResponseDTO() {}

    public ScheduleResponseDTO(ScheduleEntity entity) {
        this.id = entity.getId();
        this.clinic = new ClinicDTO(entity.getClinic());
        this.dayOfWeek = entity.getDayOfWeek();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
    }

    public static List<ScheduleResponseDTO> fromEntityList(List<ScheduleEntity> entities) {
        List<ScheduleResponseDTO> dtoList = new ArrayList<>();
        for (ScheduleEntity entity : entities) {
            dtoList.add(new ScheduleResponseDTO(entity));
        }
        return dtoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClinicDTO getClinic() {
        return clinic;
    }

    public void setClinic(ClinicDTO clinic) {
        this.clinic = clinic;
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
