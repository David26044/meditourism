package com.meditourism.meditourism.controller;

import com.meditourism.meditourism.entity.UserEntity;
import com.meditourism.meditourism.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<UserEntity> saveUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    /*
    * Para obtener todos los usuarios en la base de datos.
    * Llama al metodo de userService que llama e un metodo de
    * */
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
