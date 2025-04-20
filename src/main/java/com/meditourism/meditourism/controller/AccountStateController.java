package com.meditourism.meditourism.controller;

import com.meditourism.meditourism.entity.AccountStateEntity;
import com.meditourism.meditourism.service.AccountStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accountstate")
public class AccountStateController {

    @Autowired
    AccountStateService accountStateService;

    @PostMapping
    public ResponseEntity saveAccountState(@RequestBody AccountStateEntity accountStateEntity) {
        return ResponseEntity.ok(accountStateService.saveAccountState(accountStateEntity));
    }

}
