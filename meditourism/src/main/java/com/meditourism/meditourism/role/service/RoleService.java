package com.meditourism.meditourism.role.service;

import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.role.repository.RoleRepository;
import com.meditourism.meditourism.role.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con roles de usuario
 */
@Service
public class RoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    /**
     * Obtiene un rol por su ID
     * @param id ID del rol a buscar
     * @return Entidad RoleEntity con los datos del rol encontrado
     * @throws ResourceNotFoundException si no se encuentra el rol con el ID especificado
     */
    @Override
    public RoleEntity getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo rol en la base de datos
     * @param role Entidad RoleEntity con los datos del rol a guardar
     * @return Entidad RoleEntity con los datos del rol guardado
     * @throws ResourceAlreadyExistsException si ya existe un rol con el mismo nombre
     */
    @Override
    public RoleEntity saveRole(RoleEntity role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Ya existe rol con nombre: " + role.getName());
        }
        return roleRepository.save(role);
    }

    /**
     * Obtiene todos los roles disponibles
     * @return Lista de RoleEntity con todos los roles existentes
     */
    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Elimina un rol por su ID
     * @param id ID del rol a eliminar
     * @return Entidad RoleEntity con los datos del rol eliminado
     * @throws ResourceNotFoundException si no se encuentra el rol con el ID especificado
     */
    @Override
    public RoleEntity deleteRoleById(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " +id));
        roleRepository.delete(role);
        return role;
    }

    /**
     * Actualiza los datos de un rol existente
     * @param role Entidad RoleEntity con los nuevos datos del rol
     * @return Entidad RoleEntity con los datos actualizados
     * @throws ResourceNotFoundException si no se encuentra el rol con el ID especificado
     */
    @Override
    public RoleEntity updateRole(RoleEntity role) {
        if(!roleRepository.existsById(role.getId())){
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + role.getId());
        }
        return roleRepository.save(role);
    }
}