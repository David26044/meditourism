package com.meditourism.meditourism.appointment.controller;

import com.meditourism.meditourism.appointment.dto.AppointmentRequestDTO;
import com.meditourism.meditourism.appointment.dto.AppointmentResponseDTO;
import com.meditourism.meditourism.appointment.entity.AppointmentEntity;
import com.meditourism.meditourism.appointment.service.AppointmentService;
import com.meditourism.meditourism.schedule.dto.ScheduleResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByAuthenticatedUser() {
        return ResponseEntity.ok(appointmentService.getAppointmentsByAuthenticatedUser());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClinicId(clinicId));
    }

    @GetMapping("/clinic/{clinicId}/range")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClinicAndDateRange(
            @PathVariable Long clinicId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClinicAndDateRange(clinicId, start, end));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO dto) {
        AppointmentResponseDTO savedAppointment = appointmentService.saveAppointment(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedAppointment.getId())
                        .toUri())
                .body(savedAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentEntity.AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> deleteAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }
}