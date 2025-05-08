package com.meditourism.meditourism.role.service;

import com.meditourism.meditourism.role.repository.RoleRepository;
import com.meditourism.meditourism.role.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public RoleEntity getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }


}
