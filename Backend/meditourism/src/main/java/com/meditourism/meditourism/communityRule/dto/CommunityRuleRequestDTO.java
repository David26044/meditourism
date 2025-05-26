package com.meditourism.meditourism.communityRule.dto;


import jakarta.validation.constraints.NotBlank;

public class CommunityRuleRequestDTO {

    @NotBlank
    private String ruleText;

    public String getRuleText() {return ruleText;}

    public void setRuleText(String ruleText) {this.ruleText = ruleText = ruleText;}

}
