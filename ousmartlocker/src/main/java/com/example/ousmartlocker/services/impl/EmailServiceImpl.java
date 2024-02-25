package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.exception.SendingMailException;
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
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            StringBuilder msgBody = new StringBuilder("Hi " + details.getName() + ",\n" +
                    "\n" +
                    "Your OTP is " + details.getOtp() + ".\n" +
                    "\n" +
                    "Using this for unlocked Smartlocker\n" +
                    "\n" +
                    "Contact us: 0987654321");

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getMail());
            mailMessage.setText(msgBody.toString());
            mailMessage.setSubject("You have request on SmartLocker");

            mailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            throw new SendingMailException("Error while Sending Mail");
        }
    }

}
