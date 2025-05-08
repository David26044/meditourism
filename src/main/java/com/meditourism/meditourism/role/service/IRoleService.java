package com.meditourism.meditourism.role.service;

import com.meditourism.meditourism.role.entity.RoleEntity;

import java.util.List;

public interface IRoleService {
    RoleEntity getRoleById(Long id);
    RoleEntity saveRole(RoleEntity role);
    List<RoleEntity> getAllRoles();
}
