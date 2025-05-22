package com.meditourism.meditourism.clinic.controller;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinic.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/clinics")
public class ClinicController{

    @Autowired
    private ClinicService clinicService;

    @GetMapping
    public ResponseEntity<List<ClinicEntity>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicEntity> getClinicById(@PathVariable Long id){
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    @PostMapping
    public ResponseEntity<ClinicEntity> saveClinic(@RequestBody ClinicEntity clinicEntity){
        ClinicEntity savedClinic = clinicService.saveCLinic(clinicEntity);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedClinic.getId())
                        .toUri())
                .body(savedClinic);
    }

    @PutMapping
    public ResponseEntity<ClinicEntity> updateClinic(@RequestBody ClinicEntity clinic){
        return ResponseEntity.ok(clinicService.updateCLinic(clinic));
    }

    @DeleteMapping
    public ResponseEntity<ClinicEntity> deleteClinicById(@PathVariable Long id){
        return ResponseEntity.ok(clinicService.deleteClinicById(id));
    }

}
