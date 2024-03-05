package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.ChangePassDto;
import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ForgotPasswordRequest;
import com.example.ousmartlocker.dto.OuSmartLockerResp;

public interface UserService {
    OuSmartLockerResp getAllUser();

    OuSmartLockerResp addRole(Long id, int role);

    OuSmartLockerResp changePassword(ChangePassDto changePassDto);

    OuSmartLockerResp forgottenPass(ForgotPasswordRequest request);

    OuSmartLockerResp confirm(EmailInfoRequestDto emailInfoRequestDto);
}
