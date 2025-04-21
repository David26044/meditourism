package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.RoleEntity;
import com.meditourism.meditourism.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

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
