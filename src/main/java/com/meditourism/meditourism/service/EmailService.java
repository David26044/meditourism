package com.meditourism.meditourism.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
