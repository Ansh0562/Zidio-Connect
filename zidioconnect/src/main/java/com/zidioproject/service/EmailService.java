package com.zidioproject.service;
import com.zidioproject.dto.EmailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.to);
            message.setSubject(request.subject);
            message.setText(request.message);
            message.setFrom("your-email@gmail.com");  // Must be a valid, authenticated sender

            mailSender.send(message); // ✅ Use mailSender, not message.send()
            return "Email Sent Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }
}
