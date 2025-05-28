package com.meditourism.meditourism.appointment.service;

import com.meditourism.meditourism.appointment.dto.AppointmentRequestDTO;
import com.meditourism.meditourism.appointment.dto.AppointmentResponseDTO;
import com.meditourism.meditourism.appointment.entity.AppointmentEntity;
import com.meditourism.meditourism.appointment.repository.AppointmentRepository;
import com.meditourism.meditourism.auth.service.IAuthService;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.service.IClinicTreatmentService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.schedule.service.IScheduleService;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    IAuthService authService;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    IClinicTreatmentService clinicTreatmentService;
    @Autowired
    IScheduleService scheduleService;

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByAuthenticatedUser() {
        UserEntity user = authService.getAuthenticatedUser();
        List<AppointmentEntity> appointments = appointmentRepository.findAllByUserId(user.getId());

        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene citas registradas.");
        }

        return AppointmentResponseDTO.fromEntityList(appointments);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<AppointmentResponseDTO> getAppointmentByUserId(Long id) {
        List<AppointmentEntity> appointments = appointmentRepository.findAllByUserId(id);

        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene citas registradas.");
        }

        return AppointmentResponseDTO.fromEntityList(appointments);
    }


    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        return new AppointmentResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentEntity.AppointmentStatus status) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return new AppointmentResponseDTO(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByClinicAndDateRange(Long clinicId, LocalDateTime start, LocalDateTime end) {
        List<AppointmentEntity> appointments = appointmentRepository.findAllByClinicIdAndDateRange(clinicId, start, end);

        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron citas para la clínica en el rango de fechas dado.");
        }

        return AppointmentResponseDTO.fromEntityList(appointments);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByClinicId(Long clinicId) {
        List<AppointmentEntity> appointments = appointmentRepository.findAllByClinicTreatment_Clinic_Id(clinicId) ;

        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron citas para la clínica en el rango de fechas dado.");
        }

        return AppointmentResponseDTO.fromEntityList(appointments);
    }

    @Override
    public AppointmentResponseDTO deleteAppointment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        if (appointment.getStatus() == AppointmentEntity.AppointmentStatus.SCHEDULED ||
                appointment.getStatus() == AppointmentEntity.AppointmentStatus.CANCELLED) {
            appointmentRepository.delete(appointment);
        } else {
            throw new IllegalStateException("No se puede eliminar una cita que ya fue confirmada o completada.");
        }
        return new AppointmentResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO saveAppointment(AppointmentRequestDTO dto) {
        UserEntity user = authService.getAuthenticatedUser();

        // Validar que la relación tratamiento-clínica existe
        ClinicTreatmentEntity clinicTreatmentEntity = clinicTreatmentService.getClinicTreatmentEntityById(
                dto.getClinicId(), dto.getTreatmentId());

        LocalDateTime begin = dto.getBeginDate();
        LocalDateTime end = dto.getEndDate();

        // Validar que la fecha de inicio y fin no sean en el pasado
        if (begin.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en el pasado.");
        }

        // Validar que el horario de la cita esté dentro del horario laboral de la clínica
        boolean validSchedule = scheduleService.isWithinClinicSchedule(dto.getClinicId(), begin, end);
        if (!validSchedule) {
            throw new IllegalArgumentException("La cita no está dentro del horario laboral de la clínica.");
        }

        // Verificar si ya hay una cita traslapada en la misma clínica
        boolean overlapping = appointmentRepository.existsOverlappingAppointment(dto.getClinicId(), begin, end);
        if (overlapping) {
            throw new IllegalArgumentException("Ya existe una cita asignada en este horario.");
        }

        // Crear y guardar la cita
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setUser(user);
        appointmentEntity.setClinicTreatment(clinicTreatmentEntity);
        appointmentEntity.setBeginDate(begin);
        appointmentEntity.setEndDate(end);
        appointmentEntity.setNotes(dto.getNotes());

        appointmentRepository.save(appointmentEntity);
        return new AppointmentResponseDTO(appointmentEntity);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        if (dto.getBeginDate() != null) {
            appointment.setBeginDate(dto.getBeginDate());
        }
        if (dto.getEndDate() != null) {
            appointment.setEndDate(dto.getEndDate());
        }
        if (dto.getNotes() != null) {
            appointment.setNotes(dto.getNotes());
        }

        // Perform validations similar to saveAppointMent if beginDate or endDate are updated
        if (dto.getBeginDate() != null || dto.getEndDate() != null) {
            if (appointment.getBeginDate().isBefore(LocalDateTime.now()) || appointment.getEndDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("No se puede agendar una cita en el pasado.");
            }

            boolean validSchedule = scheduleService.isWithinClinicSchedule(
                    appointment.getClinicTreatment().getClinic().getId(),
                    appointment.getBeginDate(),
                    appointment.getEndDate()
            );
            if (!validSchedule) {
                throw new IllegalArgumentException("La cita no está dentro del horario laboral de la clínica.");
            }

            boolean overlapping = appointmentRepository.existsOverlappingAppointment(
                    appointment.getClinicTreatment().getClinic().getId(),
                    appointment.getBeginDate(),
                    appointment.getEndDate()
            );
            if (overlapping) {
                throw new IllegalArgumentException("Ya existe una cita asignada en este horario.");
            }
        }

        appointmentRepository.save(appointment);
        return new AppointmentResponseDTO(appointment);
    }


}
