package com.meditourism.meditourism.communityRule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CommunityRuleEntity {

    @Id
    private Long id;

    @Column(name="rule_text",nullable = false)
    private String ruleText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleText() {return ruleText;}

    public void setRuleText(String ruleText) {this.ruleText = ruleText = ruleText;}
}
