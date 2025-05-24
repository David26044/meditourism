package com.meditourism.meditourism.treatment.controller;

import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.service.ITreatmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    @Autowired
    ITreatmentService treatmentService;

    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> getAllTreatmets(){
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> getTreatmentById(@PathVariable Long id){
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TreatmentDTO> saveTreatment(@RequestBody @Valid TreatmentDTO treatment){
        TreatmentDTO savedTreatment = treatmentService.saveTreatment(treatment);
        URI location = ServletUriComponentsBuilder
                //Toma la infromacion de la URI de la solicitud actual, toma la URL de la solicitud que se está procesando
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTreatment.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedTreatment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<TreatmentDTO> deleteTreatmentById(@PathVariable Long id){
        return ResponseEntity.ok(treatmentService.deleteTreatmentById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<TreatmentDTO> updateTreatment(@PathVariable Long id, @RequestBody TreatmentDTO dto){
        return ResponseEntity.ok(treatmentService.updateTreatment(id, dto));
    }

}
