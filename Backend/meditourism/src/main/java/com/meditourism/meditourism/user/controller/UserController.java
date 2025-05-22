package com.meditourism.meditourism.user.controller;

import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import com.meditourism.meditourism.user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /* Para obtener todos los usuarios en la base de datos.
     * Llama al metodo de userService que llama un metodo de repository*/
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersResponseDTO());
    }

    /*Metodo para obtener usuario por su ID*/
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
            return ResponseEntity.ok(userService.getUserResponseDTOById(id));

    }

    /*Metodo para guardar un usuario nuevo*/
    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserDTO dto) {
        UserResponseDTO savedUser = userService.saveUser(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId())
                        .toUri())
                .body(savedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
