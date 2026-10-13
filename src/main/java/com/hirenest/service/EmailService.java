package com.hirenest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.otp.expiration-minutes:5}")
    private int otpExpirationMinutes;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String to, String purpose, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("HireNest - Verification OTP");

        message.setText(
                "Hello,\n\n"
                + "Your HireNest OTP for " + purpose + " is: " + otp
                + "\n\nThis OTP expires in "
                + otpExpirationMinutes
                + " minutes."
                + "\n\nIf you did not request this OTP, please ignore this email."
                + "\n\nRegards,\nHireNest Team"
        );

        mailSender.send(message);
    }

    public void sendTemporaryPassword(
            String to,
            String username,
            String temporaryPassword) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("HireNest - Your Account Has Been Created");

        message.setText(
                "Hello " + username + ",\n\n"
                + "Your HireNest account has been created successfully."
                + "\n\nYour temporary login details are:"
                + "\nEmail: " + to
                + "\nTemporary password: " + temporaryPassword
                + "\n\nPlease log in through the HireNest login page."
                + "\nYou must change your temporary password after your first login."
                + "\n\nFor your security, do not share these login details with anyone."
                + "\n\nRegards,\nHireNest Team"
        );

        mailSender.send(message);
    }
}

