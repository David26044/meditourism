package com.meditourism.meditourism.treatment.controller;

import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.service.ITreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TreatmentEntity>> getAllTreatmets(){
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentEntity> getTreatmentById(@PathVariable Long id){
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    @PostMapping
    public ResponseEntity<TreatmentEntity> saveTreatment(@RequestBody TreatmentEntity treatment){
        TreatmentEntity savedTreatment = treatmentService.saveTreatment(treatment);
        URI location = ServletUriComponentsBuilder
                //Toma la infromacion de la URI de la solicitud actual, toma la URL de la solicitud que se está procesando
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTreatment.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedTreatment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TreatmentEntity> deleteTreatmentById(@PathVariable Long id){
        return ResponseEntity.ok(treatmentService.deleteTreatmentById(id));
    }

    @PutMapping
    public ResponseEntity<TreatmentEntity> updateTreatment(@RequestBody TreatmentEntity treatment){
        return ResponseEntity.ok(treatmentService.updateTreatment(treatment));
    }

}
