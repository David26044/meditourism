package com.meditourism.meditourism.communityRule.service;

import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;

import java.util.List;

public interface ICommunityRuleService {

    List<CommunityRuleEntity> getAllCommunityRules();
    CommunityRuleEntity getCommunityRuleById(Long id);
    CommunityRuleEntity deleteCommunityRule(Long id);
    CommunityRuleEntity saveCommunityRule(CommunityRuleEntity communityRule);
    CommunityRuleEntity updateCommunityRule(Long id, CommunityRuleEntity communityRule);

}