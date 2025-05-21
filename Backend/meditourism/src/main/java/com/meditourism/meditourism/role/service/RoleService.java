package com.meditourism.meditourism.role.service;

import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
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

        return roleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Ya existe rol con nombre: " + role.getName());
        }
        return roleRepository.save(role);
    }

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity deleteRoleById(Long id){
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " +id));
        roleRepository.delete(role);
        return role;
    }

    @Override
    public RoleEntity updateRole(RoleEntity role){
        if(!roleRepository.existsById(role.getId())){
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + role.getId());
        }
        return roleRepository.save(role);
    }

}
