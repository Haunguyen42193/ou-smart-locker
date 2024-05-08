package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.SenderDetailDto;
import com.example.ousmartlocker.dto.EmailInfoDto;
import com.example.ousmartlocker.dto.SenderPasswordDto;
import com.example.ousmartlocker.exception.SendingMailException;
import com.example.ousmartlocker.services.SenderService;
import com.example.ousmartlocker.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SenderServiceImpl implements SenderService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    private final SmsService smsService;

    private static final String ERROR_SEND_MAIL = "Error while Sending Mail/SMS";

    @Autowired
    public SenderServiceImpl(JavaMailSender mailSender, SmsService smsService) {
        this.mailSender = mailSender;
        this.smsService = smsService;
    }

    public void sendRegisterLockerMail(SenderDetailDto details) {
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
            smsService.sendSms(details.getPhone(), msgBody);
        } catch (Exception e) {
            throw new SendingMailException(ERROR_SEND_MAIL);
        }
    }

    public void sendPasswordResetMail(SenderDetailDto senderDetailDto) {
        try {
            String msgBody = "Hi " + senderDetailDto.getName() + ",\n" +
                    "\n" +
                    "You are requesting a password reset.\n" +
                    "\n" +
                    "Please DO NOT provide the OTP code to others to protect your account.\n" +
                    "\n" +
                    "OTP: " + senderDetailDto.getOtp();
            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                    .mail(senderDetailDto.getMail())
                    .content(msgBody)
                    .subject("Request for forgotten password")
                    .build();
            sendEmail(emailInfoDto);
            smsService.sendSms(senderDetailDto.getPhone(), msgBody);
        } catch (Exception e) {
            throw new SendingMailException(ERROR_SEND_MAIL);
        }
    }

    @Override
    public void sendNewPassword(SenderPasswordDto passwordDto) {
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
            smsService.sendSms(passwordDto.getPhone(), msgBody);
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
