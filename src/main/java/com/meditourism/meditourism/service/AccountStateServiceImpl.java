package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.AccountStateEntity;
import com.meditourism.meditourism.repository.AccountStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountStateServiceImpl implements AccountStateService {

    @Autowired
    private AccountStateRepository accountStateRepository;

    @Override
    public AccountStateEntity getAccountStateById(Long id) {
        return accountStateRepository.findById(id).orElse(null);
    }

    @Override
    public AccountStateEntity saveAccountState(AccountStateEntity accountStateEntity) {
        return accountStateRepository.save(accountStateEntity);
    }

    @Override
    public AccountStateEntity getAccountStateById(String id) {
        try {
            return getAccountStateById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
