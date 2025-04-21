package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    RoleEntity getRoleById(Long id);
    RoleEntity saveRole(RoleEntity role);
    List<RoleEntity> getAllRoles();
}
