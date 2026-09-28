package com.hirenest.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    public void sendOtp(String to, String purpose, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("HireNest - Email Verification OTP");
        message.setText("Your HireNest OTP for " + purpose + " is: " + otp +
                "\n\nThis OTP expires in 5 minutes.\n\nIf you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}
