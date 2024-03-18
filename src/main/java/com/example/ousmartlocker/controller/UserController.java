package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public OuSmartLockerResp getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/admin/role/add/{id}/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addRole(@PathVariable Long id, @PathVariable int role) {
        return userService.addRole(id, role);
    }


    @PostMapping("/change-password")
    public OuSmartLockerResp changePassword(@RequestBody ChangePassDto changePassDto) {
        return userService.changePassword(changePassDto);
    }

    @PostMapping("/forgot-password")
    public OuSmartLockerResp forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userService.forgottenPass(request);
    }

    @PostMapping("/forgot-password/confirm")
    public OuSmartLockerResp confirmResetPass(@RequestBody EmailInfoRequestDto requestDto) {
        return userService.confirm(requestDto);
    }

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS(@RequestBody MessageDetails messageDetails) {
        SpeedSMSAPI api  = new SpeedSMSAPI("JcOM8w8tjyZV_zpv4omMw7HdCN4p-INy");
        try {
            String userInfo = api.getUserInfo();
            String phone = "84899256655";
            String content = "test sms";
            int type = 2;
            String sender = "84975256347";
            String response = api.sendSMS(phone, content, type, sender);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}
