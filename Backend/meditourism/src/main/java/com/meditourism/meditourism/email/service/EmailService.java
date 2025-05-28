package com.meditourism.meditourism.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("meditourism@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Restablecer contraseña - Odontomar";
        String body = "Hola,\n\n" +
                "Has solicitado restablecer tu contraseña. " +
                "Haz clic en el siguiente enlace para continuar:\n\n" +
                "http://localhost:3000/reset-password?token=" + resetToken + "\n\n" +
                "Este enlace expirará en 2 horas.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                "Saludos,\nEquipo Odontomar";

        sendEmail(to, subject, body);
    }
}

