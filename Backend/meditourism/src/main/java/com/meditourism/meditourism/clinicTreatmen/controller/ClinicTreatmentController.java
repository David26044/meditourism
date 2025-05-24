package com.meditourism.meditourism.clinicTreatmen.controller;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.service.IClinicTreatmentService;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics-treatments")
public class ClinicTreatmentController {

    @Autowired
    IClinicTreatmentService clinicTreatmentService;

    @GetMapping
    public ResponseEntity<List<ClinicTreatmentResponseDTO>> getAllClinicTreatments(){
        return ResponseEntity.ok(clinicTreatmentService.getAllClinicTreatments());
    }

    @GetMapping("/clinic-treatment")
    public ResponseEntity<ClinicTreatmentResponseDTO> getClinicTreatmentByIds(
            @RequestParam Long clinicId,
            @RequestParam Long treatmentId
    ) {
        ClinicTreatmentDTO dto = new ClinicTreatmentDTO();
        dto.setClinicId(clinicId);
        dto.setTreatmentId(treatmentId);
        return ResponseEntity.ok(clinicTreatmentService.getClinicTreatmentById(dto));
    }


    @GetMapping("/clinics/{id}")
    public ResponseEntity<List<ClinicDTO>> getClinicsByTreatmentId(@PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getClinicsByTreatmentId(id));
    }

    @GetMapping("/treatments/{id}")
    public ResponseEntity<List<TreatmentDTO>> getTreatmentsByClinicId(@PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getTreatmentsByClinicId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClinicTreatmentResponseDTO> saveClinicTreatment(@RequestBody ClinicTreatmentDTO clinicTreatment ){
        return ResponseEntity.ok(clinicTreatmentService.saveClinicTreatment(clinicTreatment));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ClinicTreatmentResponseDTO> updateClinicTreatment(
            @RequestParam Long clinicId,
            @RequestParam Long treatmentId, @RequestBody ClinicTreatmentDTO dto
    ){
        return ResponseEntity.ok(clinicTreatmentService.deleteClinicTreatment(clinicId, treatmentId));
    }

}
