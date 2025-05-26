package com.meditourism.meditourism.communityRule.service;

import com.meditourism.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;

import java.util.List;

public interface ICommunityRuleService {

    List<CommunityRuleResponseDTO> getAllCommunityRules();
    CommunityRuleResponseDTO getCommunityRuleById(Long id);
    CommunityRuleResponseDTO deleteCommunityRule(Long id);
    CommunityRuleResponseDTO saveCommunityRule(CommunityRuleRequestDTO dto);
    CommunityRuleResponseDTO updateCommunityRule(Long id, CommunityRuleRequestDTO dto);

}