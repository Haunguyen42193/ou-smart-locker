package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.EmailPassworDto;
import com.example.ousmartlocker.exception.SendingMailException;
import com.example.ousmartlocker.dto.EmailDetailDto;
import com.example.ousmartlocker.dto.EmailInfoDto;
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

    public void sendRegisterLockerMail(EmailDetailDto details) {
        try {

            StringBuilder msgBody = new StringBuilder("Hi " + details.getName() + ",\n" +
                    "\n" +
                    "Your OTP is " + details.getOtp() + ".\n" +
                    "\n" +
                    "Using this for unlocked Smartlocker\n" +
                    "\n" +
                    "Contact us: 0987654321");
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(details.getMail())
                    .content(msgBody.toString())
                    .subject("You have request on SmartLocker")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException("Error while Sending Mail");
        }
    }

    public void sendPasswordResetMail(EmailDetailDto emailDetailDto) {
        try {
            StringBuilder msgBody = new StringBuilder("Hi " + emailDetailDto.getName() + ",\n" +
                    "\n" +
                    "You are requesting a password reset.\n" +
                    "\n" +
                    "Please DO NOT provide the OTP code to others to protect your account.\n" +
                    "\n" +
                    "OTP: " + emailDetailDto.getOtp());
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(emailDetailDto.getMail())
                    .content(msgBody.toString())
                    .subject("Request for forgotten password")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException("Error while Sending Mail");
        }
    }

    @Override
    public void sendNewPassword(EmailPassworDto passworDto) {
        try {
            StringBuilder msgBody = new StringBuilder(
                    "Hi " + passworDto.getName() + ",\n" +
                            "\n" +
                            "Thank you for your request.\n" +
                            "\n" +
                            "We have sent you a new password: " + passworDto.getNewPass() + "\n");
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(passworDto.getMail())
                    .content(msgBody.toString())
                    .subject("Request for forgotten password")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException("Error while Sending Mail");
        }
    }

    public void sendEmail(EmailInfoDto emailInfoDto) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(emailInfoDto.getMail());
        mailMessage.setText(emailInfoDto.getContent());
        mailMessage.setSubject(emailInfoDto.getSubject());
        mailSender.send(mailMessage);
    }
}
