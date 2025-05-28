package com.meditourism.meditourism.schedule.service;

import com.meditourism.meditourism.schedule.dto.ScheduleRequestDTO;
import com.meditourism.meditourism.schedule.dto.ScheduleResponseDTO;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IScheduleService {
    ScheduleResponseDTO getScheduleById(Long id);

    List<ScheduleResponseDTO> getAllSchedules();

    List<ScheduleResponseDTO> getSchedulesByClinicId(Long clinicId);

    List<ScheduleResponseDTO> getSchedulesByClinicIdAndDay(Long clinicId, Integer dayOfWeek);

    boolean isWithinClinicSchedule(Long clinicId, LocalDateTime appointmentStart, LocalDateTime appointmentEnd);

    @Transactional
    ScheduleResponseDTO createSchedule(ScheduleRequestDTO dto);

    @Transactional
    ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO dto);

    void validateScheduleTimes(LocalTime startTime, LocalTime endTime);
}
