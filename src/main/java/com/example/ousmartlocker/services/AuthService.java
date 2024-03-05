package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.LoginDto;
import com.example.ousmartlocker.dto.SignUpDto;

public interface AuthService {
    OuSmartLockerResp authenticate(LoginDto loginDto);

    OuSmartLockerResp createUser(SignUpDto signUpDto);
}
