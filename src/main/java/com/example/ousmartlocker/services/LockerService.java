package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.model.Locker;

public interface LockerService {
    OuSmartLockerResp addLocker(Locker locker);

    OuSmartLockerResp getAllLocker();

    OuSmartLockerResp senderRegisterLocker(RegisterLockerDto registerLockerDto);

    OuSmartLockerResp confirmShipperRegisterLocker(Long historyId);

    OuSmartLockerResp shipperRegisterLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp confirmSenderRegisterLocker(Long historyId);

    OuSmartLockerResp shipperRegisterSendLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp confirmShipperRegisterSendLocker(Long historyId);

    OuSmartLockerResp receiverRegisterGetLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp confirmReceiverRegisterSendLocker(Long historyId);

    OuSmartLockerResp shipperConfirmOrderLocker(Long historyId);

    OuSmartLockerResp verifyAndOpenLocker(OpenLockerRequestDto request);

    OuSmartLockerResp registerRetry(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp deleteLocker(long id);

    OuSmartLockerResp updateLocker(long id, LockerDto dto);

    OuSmartLockerResp countLocker();
}
