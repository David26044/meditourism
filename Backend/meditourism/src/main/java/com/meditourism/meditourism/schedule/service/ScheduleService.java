package com.meditourism.meditourism.schedule.service;

import com.meditourism.meditourism.clinic.service.IClinicService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.schedule.dto.ScheduleRequestDTO;
import com.meditourism.meditourism.schedule.dto.ScheduleResponseDTO;
import com.meditourism.meditourism.schedule.entity.ScheduleEntity;
import com.meditourism.meditourism.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    IClinicService clinicService;
    @Autowired
    ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponseDTO getScheduleById(Long id) {
        return  new ScheduleResponseDTO(scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el cronograma con ID: " +id)));

    }

    @Override
    public List<ScheduleResponseDTO> getAllSchedules() {
        return ScheduleResponseDTO.fromEntityList(scheduleRepository.findAll());
    }

    @Override
    public List<ScheduleResponseDTO> getSchedulesByClinicId(Long clinicId) {
        clinicService.getClinicEntityById(clinicId); // Verifica que exista
        List<ScheduleEntity> schedules = scheduleRepository.findByClinicId(clinicId);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron horarios para la clínica con ID: " + clinicId);
        }
        return ScheduleResponseDTO.fromEntityList(schedules);
    }

    @Override
    public List<ScheduleResponseDTO> getSchedulesByClinicIdAndDay(Long clinicId, Integer dayOfWeek) {
        clinicService.getClinicEntityById(clinicId); // Verifica que exista
        List<ScheduleEntity> schedules = scheduleRepository.findByClinicIdAndDayOfWeek(clinicId, dayOfWeek);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron horarios para la clínica con ID " + clinicId + " en el día " + dayOfWeek);
        }
        return ScheduleResponseDTO.fromEntityList(schedules);
    }

    @Override
    public boolean isWithinClinicSchedule(Long clinicId, LocalDateTime appointmentStart, LocalDateTime appointmentEnd) {
        // Verifica que la clínica exista
        clinicService.getClinicEntityById(clinicId);

        // Obtiene el día de la semana de la cita (1 = lunes, ..., 7 = domingo)
        int dayOfWeek = appointmentStart.getDayOfWeek().getValue();

        // Consulta los horarios de esa clínica para ese día
        List<ScheduleEntity> schedules = scheduleRepository.findByClinicIdAndDayOfWeek(clinicId, dayOfWeek);

        // Recorre cada horario y valida si la cita está contenida en alguno
        for (ScheduleEntity schedule : schedules) {
            LocalTime scheduleStart = schedule.getStartTime();
            LocalTime scheduleEnd = schedule.getEndTime();

            LocalTime appointmentStartTime = appointmentStart.toLocalTime();
            LocalTime appointmentEndTime = appointmentEnd.toLocalTime();

            // Si la cita empieza después o igual que el inicio del horario y termina antes o igual que el final del horario
            if (!appointmentStartTime.isBefore(scheduleStart) && !appointmentEndTime.isAfter(scheduleEnd)) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    @Override
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO dto) {
        validateScheduleTimes(dto.getStartTime(), dto.getEndTime());

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setClinic(clinicService.getClinicEntityById(dto.getClinicId()));
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDTO(savedSchedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO dto) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el cronograma con ID: " +id));

        validateScheduleTimes(dto.getStartTime(), dto.getEndTime());

        if (dto.getClinicId() != null) {
            schedule.setClinic(clinicService.getClinicEntityById(dto.getClinicId()));
        }
        if (dto.getDayOfWeek() != null) {
            schedule.setDayOfWeek(dto.getDayOfWeek());
        }
        if (dto.getStartTime() != null) {
            schedule.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            schedule.setEndTime(dto.getEndTime());
        }
        return new ScheduleResponseDTO(scheduleRepository.save(schedule));
    }

    @Override
    public void validateScheduleTimes(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time cannot be equal to end time");
        }
    }

}
