package com.meditourism.meditourism.auth.controller;

import com.meditourism.meditourism.auth.dto.AuthenticationRequest;
import com.meditourism.meditourism.auth.dto.JwtResponse;
import com.meditourism.meditourism.auth.service.IAuthenticationService;
import com.meditourism.meditourism.auth.service.IJwtService;
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
    private IAuthenticationService authenticationService;

    @Autowired
    private IJwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        int result = authenticationService.authenticate(request);

        switch (result) {
            case 0:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Correo electrónico no encontrado. Por favor, verifique el correo ingresado.");
            case 1:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("La contraseña es incorrecta. Intente nuevamente.");
            case 2:
                // Si la autenticación es exitosa, generar el token
                String token = jwtService.generateToken(request.getEmail());
                JwtResponse jwtResponse = new JwtResponse(token); // Creamos el objeto JwtResponse
                return ResponseEntity.ok(jwtResponse); // Devolvemos el token con respuesta 200 OK
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Ocurrió un error inesperado. Intente nuevamente más tarde.");
        }
    }
}



