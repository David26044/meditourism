package com.meditourism.meditourism.communityRule.dto;

import com.meditourism.meditourism.communityRule.entity.CommunityRuleEntity;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;

import java.util.ArrayList;
import java.util.List;

public class CommunityRuleResponseDTO {

    private Long id;

    private String ruleText;

    public CommunityRuleResponseDTO(Long id, String ruleText) {
        this.id = id;
        this.ruleText = ruleText;
    }

    public CommunityRuleResponseDTO() {}

    public CommunityRuleResponseDTO(CommunityRuleEntity entity) {
        this.id = entity.getId();
        this.ruleText = entity.getRuleText();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleText() {return ruleText;}

    public void setRuleText(String ruleText) {this.ruleText = ruleText = ruleText;}

    public static List<CommunityRuleResponseDTO> fromEntityList(List<CommunityRuleEntity> entities) {
        List<CommunityRuleResponseDTO> dtoList = new ArrayList<>();
        for (CommunityRuleEntity entity : entities) {
            dtoList.add(new CommunityRuleResponseDTO(entity));
        }
        return dtoList;
    }

}
