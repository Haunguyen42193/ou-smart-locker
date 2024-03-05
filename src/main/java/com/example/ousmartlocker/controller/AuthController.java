package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.LoginDto;
import com.example.ousmartlocker.dto.SignUpDto;
import com.example.ousmartlocker.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public OuSmartLockerResp login(@RequestBody LoginDto loginDto) {
        return authService.authenticate(loginDto);
    }

    @PostMapping("/signup")
    public OuSmartLockerResp signUp(@RequestBody SignUpDto signUpDto) {
        return authService.createUser(signUpDto);
    }
}
