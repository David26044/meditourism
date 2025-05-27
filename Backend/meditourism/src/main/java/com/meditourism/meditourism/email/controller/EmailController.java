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

    @PostMapping
    public String sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getRecipient(), request.getSubject(), request.getBody());
        return "Correo enviado exitosamente";
    }
}
