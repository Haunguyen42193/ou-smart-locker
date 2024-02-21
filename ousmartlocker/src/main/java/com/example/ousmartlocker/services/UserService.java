package com.example.ousmartlocker.services;

import com.example.ousmartlocker.model.ChangePassDto;
import com.example.ousmartlocker.model.OuSmartLockerResp;

public interface UserService {
    OuSmartLockerResp getAllUser();

    OuSmartLockerResp addRole(Long id);

    OuSmartLockerResp changePassword(ChangePassDto changePassDto);
}
