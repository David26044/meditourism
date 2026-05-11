package com.meditourism.communityRule.service;

import com.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.communityRule.entity.CommunityRuleEntity;

import java.util.List;

public interface ICommunityRuleService {

    List<CommunityRuleResponseDTO> getAllCommunityRules();
    CommunityRuleResponseDTO getCommunityRuleById(Long id);
    CommunityRuleResponseDTO deleteCommunityRule(Long id);
    CommunityRuleResponseDTO saveCommunityRule(CommunityRuleRequestDTO dto);
    CommunityRuleResponseDTO updateCommunityRule(Long id, CommunityRuleRequestDTO dto);

}