package com.meditourism.meditourism.communityRule.controller;

import com.meditourism.meditourism.communityRule.dto.CommunityRuleRequestDTO;
import com.meditourism.meditourism.communityRule.dto.CommunityRuleResponseDTO;
import com.meditourism.meditourism.communityRule.service.ICommunityRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/community-rules")
public class CommunityRuleController {

    @Autowired
    private ICommunityRuleService communityRuleService;

    @GetMapping
    public ResponseEntity<List<CommunityRuleResponseDTO>> getAllRules() {
        return ResponseEntity.ok(communityRuleService.getAllCommunityRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> getRuleById(@PathVariable Long id) {
        return ResponseEntity.ok(communityRuleService.getCommunityRuleById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CommunityRuleResponseDTO> createRule(@RequestBody CommunityRuleRequestDTO dto) {
        CommunityRuleResponseDTO savedCommunityRule = communityRuleService.saveCommunityRule(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedCommunityRule.getId())
                        .toUri())
                .body(savedCommunityRule);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> updateRule(@PathVariable Long id, @RequestBody CommunityRuleRequestDTO dto) {
        return ResponseEntity.ok(communityRuleService.updateCommunityRule(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommunityRuleResponseDTO> deleteRule(@PathVariable Long id) {
        return ResponseEntity.ok(communityRuleService.deleteCommunityRule(id));
    }
}
