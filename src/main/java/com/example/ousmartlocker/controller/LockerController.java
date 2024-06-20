package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locker")
public class LockerController {
    private final LockerService lockerService;

    @Autowired
    public LockerController(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    //Thêm locker
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLocker(@RequestBody Locker locker) {
        return lockerService.addLocker(locker);
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
    public OuSmartLockerResp confirmReceiverRegisterSendLocker(@PathVariable Long historyId) {
        return lockerService.confirmReceiverRegisterSendLocker(historyId);
    }

    @GetMapping("/all")
    public OuSmartLockerResp getAllLocker() {
        return lockerService.getAllLocker();
    }

    @PostMapping("/register/retry")
    public OuSmartLockerResp registerRetry(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.registerRetry(reRegisterLockerDto);
    }

    @PostMapping("/open-locker")
    public ResponseEntity<OuSmartLockerResp> openLocker(@RequestBody OpenLockerRequestDto request) {
        OuSmartLockerResp response = lockerService.verifyAndOpenLocker(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp deleteLocker(@PathVariable("id") long id) {
        return lockerService.deleteLocker(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp updateLocker(@PathVariable("id") long id, @RequestBody LockerDto dto) {
        return lockerService.updateLocker(id, dto);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp countLocker() {
        return lockerService.countLocker();
    }
}

