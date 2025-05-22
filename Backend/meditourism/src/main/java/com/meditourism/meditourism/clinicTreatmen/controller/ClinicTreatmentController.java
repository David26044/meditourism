package com.meditourism.meditourism.clinicTreatmen.controller;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.service.IClinicTreatmentService;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinicss-treatments")
public class ClinicTreatmentController {

    @Autowired
    IClinicTreatmentService clinicTreatmentService;

    @GetMapping
    public ResponseEntity<List<ClinicTreatmentEntity>> getAllClinicTreatments(){
        return ResponseEntity.ok(clinicTreatmentService.getAllClinicTreatments());
    }

    @GetMapping("/clinic-treatment")
    public ResponseEntity<ClinicTreatmentEntity> getClinicTreatmentByIds(
            @RequestParam Long clinicId,
            @RequestParam Long treatmentId
    ) {
        ClinicTreatmentDTO dto = new ClinicTreatmentDTO();
        dto.setClinicId(clinicId);
        dto.setTreatmentId(treatmentId);
        return ResponseEntity.ok(clinicTreatmentService.getClinicTreatmentById(dto));
    }


    @GetMapping("/clinics/{id}")
    public ResponseEntity<List<ClinicEntity>> getClinicsByTreatmentId(@PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getClinicsByTreatmentId(id));
    }

    @GetMapping("/treatments/{id}")
    public ResponseEntity<List<TreatmentEntity>> getTreatmentsByClinicId(@PathVariable Long id){
        return ResponseEntity.ok(clinicTreatmentService.getTreatmentsByClinicId(id));
    }

    @PostMapping
    public ResponseEntity<ClinicTreatmentEntity> saveClinicTreatment(@RequestBody ClinicTreatmentDTO clinicTreatment ){
        return ResponseEntity.ok(clinicTreatmentService.saveClinicTreatment(clinicTreatment));
    }



}
