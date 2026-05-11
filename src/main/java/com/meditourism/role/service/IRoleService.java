package com.meditourism.role.service;

import com.meditourism.role.entity.RoleEntity;

import java.util.List;

public interface IRoleService {
    RoleEntity getRoleById(Long id);
    RoleEntity saveRole(RoleEntity role);
    List<RoleEntity> getAllRoles();
    RoleEntity deleteRoleById(Long id);
    RoleEntity updateRole(RoleEntity role);
}
