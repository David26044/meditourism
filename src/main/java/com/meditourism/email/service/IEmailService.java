package com.meditourism.email.service;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
