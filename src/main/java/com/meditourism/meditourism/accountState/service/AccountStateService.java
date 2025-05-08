package com.meditourism.meditourism.accountState.service;

import com.meditourism.meditourism.accountState.entity.AccountStateEntity;
import com.meditourism.meditourism.accountState.repository.AccountStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountStateService implements IAccountStateService {

    @Autowired
    private AccountStateRepository accountStateRepository;

    //Retorna el Account State buscando por su ID y si no existe retorna null
    @Override
    public AccountStateEntity getAccountStateById(Long id) {
        return accountStateRepository.findById(id).orElse(null);
    }



    @Override
    public AccountStateEntity saveAccountState(AccountStateEntity accountStateEntity) {
        return accountStateRepository.save(accountStateEntity);
    }

    @Override
    public List<AccountStateEntity> getAllAccountState() {
        return accountStateRepository.findAll();
    }

}
