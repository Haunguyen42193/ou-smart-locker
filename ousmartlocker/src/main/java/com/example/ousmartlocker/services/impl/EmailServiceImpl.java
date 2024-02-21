package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.model.EmailDetails;
import com.example.ousmartlocker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(EmailDetails details)
    {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            StringBuilder msgBody = new StringBuilder("Hi " + details.getName() + ",\n" +
                    "\n" +
                    "Your OTP is " + details.getOtp() + ".\n" +
                    "\n" +
                    "Using this for unlocked Smartlocker\n" +
                    "\n" +
                    "Contact us: 0987654321");

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getMail());
            mailMessage.setText(msgBody.toString());
            mailMessage.setSubject("You have request on SmartLocker");

            // Sending the mail
            mailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}
