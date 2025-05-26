package com.meditourism.meditourism.communityRule.service;

import com.meditourism.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;
import com.meditourism.meditourism.communityRule.repository.CommunityRuleRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityRuleService implements ICommunityRuleService {

    @Autowired
    private CommunityRuleRepository communityRuleRepository;

    /**
     * @return 
     */
    @Override
    public List<CommunityRuleResponseDTO> getAllCommunityRules() {
        return CommunityRuleResponseDTO.fromEntityList(communityRuleRepository.findAll());
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public CommunityRuleResponseDTO getCommunityRuleById(Long id) {
        return new CommunityRuleResponseDTO(communityRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la regla de comunidad con id: " + id)));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public CommunityRuleResponseDTO deleteCommunityRule(Long id) {
        CommunityRuleEntity entity = communityRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la regla de comunidad con id: " + id));
        communityRuleRepository.delete(entity);
        return new CommunityRuleResponseDTO(entity);
    }

    /**
     * @param dto 
     * @return
     */
    @Override
    public CommunityRuleResponseDTO saveCommunityRule(CommunityRuleRequestDTO dto) {
        CommunityRuleEntity entity = new CommunityRuleEntity();
        entity.setRuleText(dto.getRuleText());
        return new CommunityRuleResponseDTO(communityRuleRepository.save(entity));
    }

    /**
     * @param id 
     * @param dto
     * @return
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
