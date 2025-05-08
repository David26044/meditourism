package com.meditourism.meditourism.accountState.service;

import com.meditourism.meditourism.accountState.entity.AccountStateEntity;

import java.util.List;

public interface IAccountStateService {
    AccountStateEntity getAccountStateById(Long id);
    AccountStateEntity saveAccountState(AccountStateEntity accountStateEntity);
    List<AccountStateEntity> getAllAccountState();
}
