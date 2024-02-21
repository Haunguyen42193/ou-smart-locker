package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.entity.Locker;
import com.example.ousmartlocker.entity.Otp;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.exception.UserNotFoundException;
import com.example.ousmartlocker.model.EmailDetails;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.repository.LockerRepository;
import com.example.ousmartlocker.repository.OtpRepository;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.services.EmailService;
import com.example.ousmartlocker.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class LockerServiceImpl implements LockerService {
    @Autowired
    private LockerRepository lockerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public OuSmartLockerResp addlocker(Locker locker) {
        locker.setIsOccupied(false);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerRepository.save(locker)).build();
    }

    @Override
    public OuSmartLockerResp getAlllocker() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerRepository.findAll()).build();
    }
    @Override
    public OuSmartLockerResp registerLocker(Long userId) {
        List<Locker> availableLockers = lockerRepository.findByIsOccupiedFalse();
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        if (availableLockers.isEmpty()) {
            // Xử lý khi không còn locker trống
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker fail").data("Không còn locker trống hoặc đăng ký không thành công").build();
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(availableLockers.size());
            Locker selectedLocker = availableLockers.get(randomIndex);
            selectedLocker.setUser(user); // userRepository là repository cho User
            selectedLocker.setIsOccupied(true);
           //selectedLocker.setOccupied(new Date());
            lockerRepository.save(selectedLocker);

            Otp otp = this.generateOTP(selectedLocker);

            EmailDetails emailDetails = EmailDetails.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .otp(otp.getOtpNumber()).build();

            emailService.sendSimpleMail(emailDetails);

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data("Đăng ký locker thành công").build();
        }
    }

    private Otp generateOTP(Locker locker) {
        Random random = new Random();
        int otpValue = 1000 + random.nextInt(9000);
        String otp = String.valueOf(otpValue);

        // Save the new OTP information in the database
        Otp otpInfo = Otp.builder()
                .otpNumber(otp)
                .setGeneratedAt(String.valueOf(LocalDateTime.now()))
                .locker(locker).build();

        otpRepository.save(otpInfo);

        return otpInfo;
    }
}
