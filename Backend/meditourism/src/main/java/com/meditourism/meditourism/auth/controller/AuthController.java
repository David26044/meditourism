package com.meditourism.meditourism.auth.controller;

import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.service.IAuthService;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.service.IUserService;
import com.meditourism.meditourism.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserDTO request){
        return ResponseEntity.ok(authService.register(request));
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
//
//
//        int result = authenticationService.authenticate(request);
//
//
//        switch (result) {
//            case 0:
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Correo electrónico no encontrado. Por favor, verifique el correo ingresado.");
//            case 1:
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("La contraseña es incorrecta. Intente nuevamente.");
//            case 2:
//                // Si la autenticación es exitosa, generar el token
//                String token = jwtService.generateToken(request.getEmail());
//                JwtResponse jwtResponse = new JwtResponse(token); // Creamos el objeto JwtResponse
//                return ResponseEntity.ok(jwtResponse); // Devolvemos el token con respuesta 200 OK
//            default:
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Ocurrió un error inesperado. Intente nuevamente más tarde.");
//        }
//    }

    @GetMapping
    public String holaMUndo(){
        return "Hola mundo";
    }
}



