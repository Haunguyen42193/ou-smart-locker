package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.exception.TokenInvalidException;
import com.example.ousmartlocker.exception.UserNotFoundException;
import com.example.ousmartlocker.model.*;
import com.example.ousmartlocker.repository.*;
import com.example.ousmartlocker.services.EmailService;
import com.example.ousmartlocker.services.LockerService;
import com.example.ousmartlocker.util.ReadToken;
import com.example.ousmartlocker.util.SmartLockerUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class LockerServiceImpl implements LockerService {
    @Autowired
    private LockerRepository lockerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private LockerLocationRepository lockerLocationRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReadToken readToken;

    @Override
    public OuSmartLockerResp addlocker(Locker locker) {
        locker.setIsOccupied(false);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerRepository.save(locker)).build();
    }

    @Override
    public OuSmartLockerResp getAlllocker() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all locker successful").data(lockerRepository.findAll()).build();
    }
    @Override
    public OuSmartLockerResp registerLocker(RegisterLockerDto registerLockerDto) {
        List<Locker> availableLockers = lockerRepository.findByIsOccupiedFalseAndLockerLocation(registerLockerDto.getLocationSend());
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        if (availableLockers.isEmpty()) {
            // Xử lý khi không còn locker trống
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Register locker fail").data("Không còn locker trống hoặc đăng ký không thành công").build();
        } else {
            SecureRandom random = new SecureRandom();
            int randomIndex = random.nextInt(availableLockers.size());
            Locker selectedLocker = availableLockers.get(randomIndex);
            selectedLocker.setIsOccupied(true);
            lockerRepository.save(selectedLocker);

            Otp otp = this.generateOTP(selectedLocker);

            LocalDateTime endTime = SmartLockerUtils.currentTime.plusHours(1);

            History history = History.builder()
                    .lockerId(selectedLocker.getLockerId())
                    .startTime(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime))
                    .endTime(SmartLockerUtils.formatter.format(endTime))
                    .shipper(registerLockerDto.getShipper())
                    .receiver(registerLockerDto.getReceiver())
                    .userSend(Long.valueOf(userId))
                    .build();

            historyRepository.save(history);

            EmailDetailDto emailDetailDto = EmailDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .otp(otp.getOtpNumber()).build();

            emailService.sendRegisterLockerMail(emailDetailDto);

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Register locker successful").data("Đăng ký locker thành công").build();
        }
    }

    @Override
    public OuSmartLockerResp confirm(EmailInfoRequestDto emailInfoRequestDto) {

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Confirm success").build();
    }

    @Override
    public OuSmartLockerResp reRegisterLocker(ReRegisterLockerDto reRegisterLockerDto) {
        Locker locker = lockerRepository.findById(reRegisterLockerDto.getLockerId()).orElse(null);
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        if (Objects.isNull(locker)) {
            // Xử lý khi không không tìm thấy locker
            return OuSmartLockerResp.builder().status(HttpStatus.NOT_FOUND).message("Not found locker").data("Not found locker").build();
        } else {
            locker.setIsOccupied(true);
            lockerRepository.save(locker);

            Otp otp = this.generateOTP(locker);

            EmailDetailDto emailDetailDto = EmailDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .otp(otp.getOtpNumber()).build();

            emailService.sendRegisterLockerMail(emailDetailDto);

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Re-register locker successful").data("Đăng ký locker thành công").build();
        }
    }

    @Override
    public OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation) {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerLocationRepository.save(lockerLocation)).build();
    }

    private Otp generateOTP(Locker locker) {
        SecureRandom random = new SecureRandom();
        int otpValue = 1000 + random.nextInt(9000);
        String otp = String.valueOf(otpValue);

        LocalDateTime expireTime = SmartLockerUtils.currentTime.plusMinutes(15);

        Otp otpInfo = Otp.builder()
                .otpNumber(otp)
                .setGeneratedAt(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime))
                .locker(locker)
                .expireTime(SmartLockerUtils.formatter.format(expireTime))
                .build();

        otpRepository.save(otpInfo);

        return otpInfo;
    }
}
