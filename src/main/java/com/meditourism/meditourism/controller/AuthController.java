package com.meditourism.meditourism.controller;


import com.meditourism.meditourism.dto.AuthenticationRequest;
import com.meditourism.meditourism.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthenticationRequest request) {
        int result = authenticationService.authenticate(request);

        switch (result) {
            case 0:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //rmail no encontrado
            case 1:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //clave incorrecta
            case 2:
                return ResponseEntity.ok().build(); // Login exitoso
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //otro error inesperado
        }
    }
}


