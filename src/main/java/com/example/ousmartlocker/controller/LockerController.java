package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.dto.RegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locker")
public class LockerController {
    @Autowired
    private LockerService lockerService;

    //Thêm locker
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLocker(@RequestBody Locker locker) {
        return lockerService.addLocker(locker);
    }

    //Thêm địa điểm
    @PostMapping("/location/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLockerLocation(@RequestBody LockerLocation lockerLocation) {
        return lockerService.addLockerLocation(lockerLocation);
    }

    //Đăng ký locker
    @PostMapping("/register/sender/send")
    public OuSmartLockerResp senderRegisterLocker(@RequestBody RegisterLockerDto registerLockerDto) {
        return lockerService.senderRegisterLocker(registerLockerDto);
    }

    //Confirm của người gửi
    @GetMapping("/register/sender/send/confirm/{historyId}")
    public OuSmartLockerResp confirmSenderRegisterLocker(@PathVariable Long historyId) {
        return lockerService.confirmSenderRegisterLocker(historyId);
    }

    //Xác nhận nhận đơn của shipper
    @GetMapping("/register/shipper/confirm-order/{historyId}")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp shipperRegisterLocker(@PathVariable Long historyId) {
        return lockerService.shipperConfirmOrderLocker(historyId);
    }

    //Đăng kí của shipper
    @PostMapping("/register/shipper/get")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp shipperRegisterLocker(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.shipperRegisterLocker(reRegisterLockerDto);
    }

    //Confirm của shipper
    @GetMapping("/register/shipper/get/confirm/{historyId}")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp confirmShipperRegisterLocker(@PathVariable Long historyId) {
        return lockerService.confirmShipperRegisterLocker(historyId);
    }

    //Đăng kí tủ gửi hàng của shipper
    @PostMapping("/register/shipper/send")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp shipperRegisterSendLocker(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.shipperRegisterSendLocker(reRegisterLockerDto);
    }

    //confirm tủ gửi hàng của shipper
    @GetMapping("/register/shipper/send/confirm/{historyId}")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp confirmShipperRegisterSendLocker(@PathVariable Long historyId) {
        return lockerService.confirmShipperRegisterSendLocker(historyId);
    }

    //Đăng kí tủ nhận hàng của người nhận
    @PostMapping("/register/receiver/get")
    public OuSmartLockerResp receiverRegisterGetLocker(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.receiverRegisterGetLocker(reRegisterLockerDto);
    }

    //Confirm tủ nhận hàng của người nhận
    @GetMapping("/register/receiver/get/confirm/{historyId}")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp confirmReceiverRegisterSendLocker(@PathVariable Long historyId) {
        return lockerService.confirmReceiverRegisterSendLocker(historyId);
    }

    @GetMapping("/all/locker")
    public OuSmartLockerResp getAllLocker() {
        return lockerService.getAllLocker();
    }

    @GetMapping("/all/location")
    public OuSmartLockerResp getAllLocation() {
        return lockerService.getAllLocation();
    }

    @GetMapping("/history/{historyId}")
    public OuSmartLockerResp getHistoryById(@PathVariable Long historyId) {
        return lockerService.getHistoryById(historyId);
    }

    @GetMapping("/history/all")
    public OuSmartLockerResp getAllHistory() {
        return lockerService.getAllHistory();
    }
}

