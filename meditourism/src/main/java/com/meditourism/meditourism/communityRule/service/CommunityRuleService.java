package com.meditourism.meditourism.communityRule.service;

import com.meditourism.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;
import com.meditourism.meditourism.communityRule.repository.CommunityRuleRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para manejar las operaciones relacionadas con las reglas de la comunidad.
 */
@Service
public class CommunityRuleService implements ICommunityRuleService {

    @Autowired
    private CommunityRuleRepository communityRuleRepository;

    /**
     * Obtiene todas las reglas de la comunidad.
     *
     * @return Lista de DTOs de respuesta con todas las reglas de la comunidad
     */
    @Override
    public List<CommunityRuleResponseDTO> getAllCommunityRules() {
        return CommunityRuleResponseDTO.fromEntityList(communityRuleRepository.findAll());
    }

    /**
     * Obtiene una regla de la comunidad por su ID.
     *
     * @param id Identificador único de la regla de comunidad
     * @return DTO de respuesta con la regla de comunidad encontrada
     * @throws ResourceNotFoundException Si no se encuentra la regla con el ID especificado
     */
    @Override
    public CommunityRuleResponseDTO getCommunityRuleById(Long id) {
        return new CommunityRuleResponseDTO(communityRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la regla de comunidad con id: " + id)));
    }

    /**
     * Elimina una regla de la comunidad por su ID.
     *
     * @param id Identificador único de la regla de comunidad a eliminar
     * @return DTO de respuesta con la regla de comunidad eliminada
     * @throws ResourceNotFoundException Si no se encuentra la regla con el ID especificado
     */
    @Override
    public CommunityRuleResponseDTO deleteCommunityRule(Long id) {
        CommunityRuleEntity entity = communityRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la regla de comunidad con id: " + id));
        communityRuleRepository.delete(entity);
        return new CommunityRuleResponseDTO(entity);
    }

    /**
     * Guarda una nueva regla de comunidad.
     *
     * @param dto DTO de solicitud con los datos de la nueva regla
     * @return DTO de respuesta con la regla de comunidad guardada
     */
    @Override
    public CommunityRuleResponseDTO saveCommunityRule(CommunityRuleRequestDTO dto) {
        CommunityRuleEntity entity = new CommunityRuleEntity();
        entity.setRuleText(dto.getRuleText());
        return new CommunityRuleResponseDTO(communityRuleRepository.save(entity));
    }

    /**
     * Actualiza una regla de comunidad existente.
     *
     * @param id Identificador único de la regla de comunidad a actualizar
     * @param dto DTO de solicitud con los nuevos datos de la regla
     * @return DTO de respuesta con la regla de comunidad actualizada
     * @throws ResourceNotFoundException Si no se encuentra la regla con el ID especificado
     */
    @Override
    public CommunityRuleResponseDTO updateCommunityRule(Long id, CommunityRuleRequestDTO dto) {
        CommunityRuleEntity entity = communityRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la regla de comunidad con id: " + id));

        if (dto.getRuleText() != null) {
            entity.setRuleText(dto.getRuleText());
        }
        return new CommunityRuleResponseDTO(communityRuleRepository.save(entity));
    }
}