package com.example.ousmartlocker.services;

import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.payload.LoginDto;
import com.example.ousmartlocker.payload.SignUpDto;

public interface AuthService {
    OuSmartLockerResp authenticate(LoginDto loginDto);

    OuSmartLockerResp createUser(SignUpDto signUpDto);
}
