package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.EmailDetailDto;
import com.example.ousmartlocker.dto.EmailInfoDto;
import com.example.ousmartlocker.dto.EmailPasswordDto;

public interface EmailService {
    void sendRegisterLockerMail(EmailDetailDto details);
    void sendPasswordResetMail (EmailDetailDto emailDetailDto);
    void sendNewPassword(EmailPasswordDto passworDto);
    void sendEmail (EmailInfoDto emailInfoDto);
}
