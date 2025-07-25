package com.meditourism.meditourism.communityRule.entity;

import jakarta.persistence.*;

@Entity
@Table(name="community_rules")
public class CommunityRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
