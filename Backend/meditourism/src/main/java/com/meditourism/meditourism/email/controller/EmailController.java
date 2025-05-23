package com.meditourism.meditourism.email.controller;

import com.meditourism.meditourism.email.dto.EmailRequest;
import com.meditourism.meditourism.email.service.IEmailService;
import com.meditourism.meditourism.jwt.IJwtService;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private IEmailService emailService;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private IUserService userService;

    @Value("${app.email.verification-url}")
    private String verificationUrlBase;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(Authentication authentication) {
        // 1. Obtener el email del usuario autenticado
        String email = authentication.getName();

        // 2. Generar el token JWT con el email
        String token = jwtService.generateVerificationToken(email);

        // 3. Construir el link de verificación
        String verificationLink = verificationUrlBase +  "?token=" + token;

        // 4. Enviar el correo
        String subject = "Verifica tu correo electrónico";
        String body = "Bienvenido a MediTourism. Haz clic en el siguiente enlace para verificar tu correo:\n\n" + verificationLink;
        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok("Correo de verificación enviado a " + email);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        try {
            // 1. Extraer el email del token
            String email = jwtService.getUsernameFromToken(token);

            // 2. Verificar el usuario (cambiar isVerified a true)
            userService.verifyEmail(email);

            // 3. Respuesta exitosa
            return ResponseEntity.ok("Correo verificado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token inválido o expirado.");
        }
    }


    @PostMapping
    public String sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getRecipient(), request.getSubject(), request.getBody());
        return "Correo enviado exitosamente";
    }
}
