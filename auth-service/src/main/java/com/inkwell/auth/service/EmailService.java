package com.inkwell.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("InkWell - Verify your email");
        String verificationLink = frontendUrl + "/verify-email?token=" + token;
        message.setText("Welcome to InkWell!\n\nPlease verify your email by clicking the link below:\n" + verificationLink + "\n\nThis link will expire in 24 hours.");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("InkWell - Password Reset Request");
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        message.setText("You requested a password reset.\n\nPlease click the link below to reset your password:\n" + resetLink + "\n\nThis link will expire in 1 hour.\nIf you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}
