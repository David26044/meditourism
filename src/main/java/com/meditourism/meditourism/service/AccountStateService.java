package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.AccountStateEntity;

public interface AccountStateService {
    AccountStateEntity getAccountStateById(Long id);
    AccountStateEntity saveAccountState(AccountStateEntity accountStateEntity);
}
