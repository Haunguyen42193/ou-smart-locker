package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.EmailDetailDto;
import com.example.ousmartlocker.dto.EmailInfoDto;
import com.example.ousmartlocker.dto.EmailPasswordDto;
import com.example.ousmartlocker.exception.SendingMailException;
import com.example.ousmartlocker.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    private static final String ERROR_SEND_MAIL = "Error while Sending Mail";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegisterLockerMail(EmailDetailDto details) {
        try {

            String msgBody = "Hi " + details.getName() + ",\n" +
                    "\n" +
                    "Your OTP is " + details.getOtp() + ".\n" +
                    "\n" +
                    "Using this for unlocked SmartLocker\n" +
                    "\n" +
                    "Contact us: 0987654321";
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(details.getMail())
                    .content(msgBody)
                    .subject("You have request on SmartLocker")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException(ERROR_SEND_MAIL);
        }
    }

    public void sendPasswordResetMail(EmailDetailDto emailDetailDto) {
        try {
            String msgBody = "Hi " + emailDetailDto.getName() + ",\n" +
                    "\n" +
                    "You are requesting a password reset.\n" +
                    "\n" +
                    "Please DO NOT provide the OTP code to others to protect your account.\n" +
                    "\n" +
                    "OTP: " + emailDetailDto.getOtp();
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(emailDetailDto.getMail())
                    .content(msgBody)
                    .subject("Request for forgotten password")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException(ERROR_SEND_MAIL);
        }
    }

    @Override
    public void sendNewPassword(EmailPasswordDto passwordDto) {
        try {
            String msgBody = "Hi " + passwordDto.getName() + ",\n" +
                    "\n" +
                    "Thank you for your request.\n" +
                    "\n" +
                    "We have sent you a new password: " + passwordDto.getNewPass() + "\n";
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(passwordDto.getMail())
                    .content(msgBody)
                    .subject("Request for forgotten password")
                    .build();
            sendEmail(emailInfoDto);
        } catch (Exception e) {
            throw new SendingMailException(ERROR_SEND_MAIL);
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
