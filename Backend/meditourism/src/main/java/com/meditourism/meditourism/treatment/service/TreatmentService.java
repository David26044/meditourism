package com.meditourism.meditourism.treatment.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.service.ClinicTreatmentService;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreatmentService implements ITreatmentService{

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private ClinicTreatmentService clinicTreatmentService;

    @Override
    public TreatmentDTO saveTreatment(TreatmentDTO dto) {
        if (treatmentRepository.existsByName(dto.getName())){
            new ResourceAlreadyExistsException("Tratamiento con nombre '" + dto.getName() + "' ya existe");
        }

        TreatmentEntity treatment = new TreatmentEntity();

        treatment.setDescription(dto.getDescription());
        treatment.setName(dto.getName());

        return new TreatmentDTO(treatmentRepository.save(treatment));
    }

    @Override
    public TreatmentDTO updateTreatment(Long id, TreatmentDTO dto) {

        TreatmentEntity treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + dto.getId()));

        if (dto.getDescription() != null){
            treatment.setDescription(dto.getDescription());
        }
        if (dto.getName() != null){
            treatment.setName(dto.getName());
        }
        return new TreatmentDTO(treatmentRepository.save(treatment));
    }

    @Override
    public TreatmentDTO getTreatmentById(Long id) {
        return new TreatmentDTO(treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id)));
    }

    @Override
    public TreatmentDTO deleteTreatmentById(Long id) {
        TreatmentEntity treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id));
        treatmentRepository.delete(treatment);
        return new TreatmentDTO(treatment);
    }

    @Override
    public List<TreatmentDTO> getAllTreatments() {
        List<TreatmentEntity> treatments = treatmentRepository.findAll();
        return TreatmentDTO.fromEntityList(treatments);
    }
}
