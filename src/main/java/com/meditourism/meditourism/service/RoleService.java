package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.RoleEntity;

public interface RoleService {
    RoleEntity getRoleById(Long id);
    RoleEntity saveRole(RoleEntity role);
}
