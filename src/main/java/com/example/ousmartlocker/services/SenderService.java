package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.SenderDetailDto;
import com.example.ousmartlocker.dto.EmailInfoDto;
import com.example.ousmartlocker.dto.SenderPasswordDto;

public interface SenderService {
    void sendRegisterLockerMail(SenderDetailDto details);
    void sendPasswordResetMail (SenderDetailDto senderDetailDto);
    void sendNewPassword(SenderPasswordDto passworDto);
    void sendEmail (EmailInfoDto emailInfoDto);
}
