package com.meditourism.meditourism.role.controller;

import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/role")
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

    // Crear un rol
    @PostMapping
    public ResponseEntity<RoleEntity> saveRole(@RequestBody RoleEntity role) {
        RoleEntity savedRole = roleService.saveRole(role);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedRole.getId())
                        .toUri())
                .body(savedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleEntity> deleteRoleById(@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteRoleById(id));
    }

    @PutMapping
    public ResponseEntity<RoleEntity> updateRole(@RequestBody RoleEntity role){
        return ResponseEntity.ok(roleService.updateRole(role));
    }
}

