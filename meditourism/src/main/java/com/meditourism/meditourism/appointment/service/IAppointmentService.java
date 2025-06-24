package com.meditourism.meditourism.appointment.service;

import com.meditourism.meditourism.appointment.dto.AppointmentRequestDTO;
import com.meditourism.meditourism.appointment.dto.AppointmentResponseDTO;
import com.meditourism.meditourism.appointment.entity.AppointmentEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAppointmentsByAuthenticatedUser();

    List<AppointmentResponseDTO> getAppointmentByUserId(Long id);

    AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentEntity.AppointmentStatus status);

    List<AppointmentResponseDTO> getAppointmentsByClinicAndDateRange(Long clinicId, LocalDateTime start, LocalDateTime end);

    List<AppointmentResponseDTO> getAppointmentsByClinicId(Long clinicId);

    AppointmentResponseDTO deleteAppointment(Long id);

    AppointmentResponseDTO saveAppointment(AppointmentRequestDTO dto);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);
}
