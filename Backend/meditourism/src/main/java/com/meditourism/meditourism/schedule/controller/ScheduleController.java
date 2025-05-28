package com.meditourism.meditourism.schedule.controller;

import com.meditourism.meditourism.schedule.dto.ScheduleRequestDTO;
import com.meditourism.meditourism.schedule.dto.ScheduleResponseDTO;
import com.meditourism.meditourism.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByClinicId(clinicId));
    }

    @GetMapping("/clinic/{clinicId}/day/{dayOfWeek}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByClinicIdAndDay(
            @PathVariable Long clinicId,
            @PathVariable Integer dayOfWeek) {
        return ResponseEntity.ok(scheduleService.getSchedulesByClinicIdAndDay(clinicId, dayOfWeek));
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> postSchedule(@Valid @RequestBody ScheduleRequestDTO dto) {
        ScheduleResponseDTO savedSchedule = scheduleService.createSchedule(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("{/id}")
                        .buildAndExpand(savedSchedule.getId())
                        .toUri())
                .body(savedSchedule);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> patchSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, dto));
    }
}