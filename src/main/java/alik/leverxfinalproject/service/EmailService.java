package alik.leverxfinalproject.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String toEmail, String confirmationToken) {
        String confirmationUrl = "http://localhost:8080/auth/confirm_email?token=" + confirmationToken;
        String subject = "Confirm Your Email";
        String body = "Please click the link below to confirm your email address:\n" + confirmationUrl +
                "\nThis link will expire in 24 hours.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Password Reset Request";
        String body = "You have requested to reset your password. Here is your confirmation code to reset your password:\n" + resetCode +
                "\nThis link will expire in 24 hours.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }



}
