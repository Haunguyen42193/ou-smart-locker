package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.*;

public interface UserService {
    OuSmartLockerResp getAllUser();

    OuSmartLockerResp addRole(Long id, int role);

    OuSmartLockerResp changePassword(ChangePassDto changePassDto);

    OuSmartLockerResp forgottenPass(ForgotPasswordRequest request);

    OuSmartLockerResp confirm(EmailInfoRequestDto emailInfoRequestDto);

    OuSmartLockerResp updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    OuSmartLockerResp getRecordLogin(String startDate, String endDate);

}
