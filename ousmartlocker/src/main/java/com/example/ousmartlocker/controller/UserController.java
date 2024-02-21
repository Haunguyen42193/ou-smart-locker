package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.model.ChangePassDto;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/role/admin/add/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addRoleAdmin(@PathVariable Long id) {
        return userService.addRole(id);
    }

    @PostMapping("/change-password")
    public OuSmartLockerResp changePassword(@RequestBody ChangePassDto changePassDto) {
        return userService.changePassword(changePassDto);
    }
}
