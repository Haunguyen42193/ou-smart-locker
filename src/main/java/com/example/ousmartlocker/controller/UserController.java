package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.ChangePassDto;
import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ForgotPasswordRequest;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


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

    @GetMapping("/datetime")
    public OuSmartLockerResp getLocalDateTime() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).data(LocalDateTime.now().toString()).build();
    }
}
