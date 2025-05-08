package com.meditourism.meditourism.accountState.controller;

import com.meditourism.meditourism.accountState.entity.AccountStateEntity;
import com.meditourism.meditourism.accountState.service.IAccountStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountstate")
public class AccountStateController {

    @Autowired
    IAccountStateService accountStateService;

    @GetMapping
    public ResponseEntity<List<AccountStateEntity>> getAllAccountState() {
        return ResponseEntity.ok(accountStateService.getAllAccountState());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountStateEntity> getAccountStateById(@PathVariable Long id) {
        AccountStateEntity accountState = accountStateService.getAccountStateById(id);
        if (accountState != null) {
            return ResponseEntity.ok(accountState);
        }
        return ResponseEntity.notFound().build();
    }


    /*Para guardar un nuevo estado de cuenta*/
    @PostMapping
    public ResponseEntity saveAccountState(@RequestBody AccountStateEntity accountStateEntity) {
        return ResponseEntity.ok(accountStateService.saveAccountState(accountStateEntity));
    }

}
