package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.AccountStateEntity;

import java.util.List;

public interface AccountStateService {
    AccountStateEntity getAccountStateById(Long id);
    AccountStateEntity saveAccountState(AccountStateEntity accountStateEntity);
    List<AccountStateEntity> getAllAccountState();
}
