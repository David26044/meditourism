package com.meditourism.meditourism.auth.controller;

import com.meditourism.meditourism.auth.dto.AuthResponse;
import com.meditourism.meditourism.auth.dto.AuthRequest;
import com.meditourism.meditourism.auth.dto.ChangePasswordDTO;
import com.meditourism.meditourism.auth.service.IAuthService;
import com.meditourism.meditourism.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/send-change-password")
    public ResponseEntity<String> sendChangePassword(@RequestParam("email") String email) {
        authService.sendEmailChangePassword(email);
        return ResponseEntity.ok("Correo de recuperación enviado con éxito.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("token") String token,
                                                 @RequestBody ChangePasswordDTO dto) {
        authService.changePassword(token, dto);
        return ResponseEntity.ok("Contraseña cambiada con éxito.");
    }

    @PostMapping("/send-verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        authService.sendEmailVerification(email);
        return ResponseEntity.ok("Correo de verificación enviado a " + email);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Correo verificado correctamente.");
    }

}



