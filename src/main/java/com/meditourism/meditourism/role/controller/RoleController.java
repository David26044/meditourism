package com.meditourism.meditourism.role.controller;

import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> getRoleById(@PathVariable Long id) {
        RoleEntity role = roleService.getRoleById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    /*Obtener todos los roles*/
    @GetMapping
    public ResponseEntity<List<RoleEntity>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Crear o actualizar un rol
    @PostMapping
    public ResponseEntity<RoleEntity> saveRole(@RequestBody RoleEntity role) {
        RoleEntity savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

//    // Eliminar un rol
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
//        RoleEntity existingRole = roleService.getRoleById(id);
//        if (existingRole != null) {
//            roleService.deleteRole(id);
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}

