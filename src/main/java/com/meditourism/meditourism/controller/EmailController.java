package com.meditourism.meditourism.controller;

import com.meditourism.meditourism.dto.EmailRequest;
import com.meditourism.meditourism.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send-welcome-email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public String sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getRecipient(), request.getSubject(), request.getBody());
        return "Correo enviado exitosamente";
    }
}
