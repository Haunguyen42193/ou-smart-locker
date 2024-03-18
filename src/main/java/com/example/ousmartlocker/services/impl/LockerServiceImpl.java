package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.exception.*;
import com.example.ousmartlocker.model.*;
import com.example.ousmartlocker.model.enums.HistoryLocationRole;
import com.example.ousmartlocker.model.enums.HistoryUserRole;
import com.example.ousmartlocker.model.enums.Role;
import com.example.ousmartlocker.repository.*;
import com.example.ousmartlocker.services.EmailService;
import com.example.ousmartlocker.services.LockerService;
import com.example.ousmartlocker.util.ReadToken;
import com.example.ousmartlocker.util.SmartLockerUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

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
    private HistoryUserRepository historyUserRepository;
    @Autowired
    private HistoryLocationRepository historyLocationRepository;
    @Autowired
    private LockerOtpRepository lockerOtpRepository;
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
    @Transactional
    public OuSmartLockerResp registerLocker(RegisterLockerDto registerLockerDto) {
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        User receiveUser = userRepository.findById(registerLockerDto.getReceiver()).orElse(null);
        if (Objects.isNull(receiveUser))
            throw new UserNotFoundException("Receive user not found!");
        LockerLocation locationSend = lockerLocationRepository.findById(registerLockerDto.getLocationSend()).orElse(null);
        if (Objects.isNull(locationSend))
            throw new LocationNotFoundException("Location send not found!");
        LockerLocation locationReceive = lockerLocationRepository.findById(registerLockerDto.getLocationReceive()).orElse(null);
        if (Objects.isNull(locationReceive))
            throw new LocationNotFoundException("Location receive not found!");
        List<Locker> availableLockers = lockerRepository.findByIsOccupiedFalseAndLockerLocation(registerLockerDto.getLocationSend());
        if (availableLockers.isEmpty()) {
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Register locker fail").data("Không còn locker trống hoặc đăng ký không thành công").build();
        } else {
            SecureRandom random = new SecureRandom();
            int randomIndex = random.nextInt(availableLockers.size());
            Locker selectedLocker = availableLockers.get(randomIndex);
            selectedLocker.setIsOccupied(true);
            lockerRepository.save(selectedLocker);

            Otp otp = this.generateOTP(selectedLocker);

            LocalDateTime localDateTime = SmartLockerUtils.currentTime.plusHours(1);
            String endTime = SmartLockerUtils.formatter.format(localDateTime);
            String startTime = SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime);
            History history = History.builder()
                    .locker(selectedLocker)
                    .startTime(startTime)
                    .endTime(endTime)
                    .otp(otp)
                    .build();

            HistoryUser historySender = HistoryUser.builder().user(user).role(HistoryUserRole.SENDER).history(history).build();
            HistoryUser historyReceiver = HistoryUser.builder().user(receiveUser).role(HistoryUserRole.RECEIVER).history(history).build();
            List<HistoryUser> historyUsers = new ArrayList<>();
            historyUsers.add(historySender);
            historyUsers.add(historyReceiver);
            history.setUsers(historyUsers);

            HistoryLocation historyLocationSend = HistoryLocation.builder().location(locationSend).role(HistoryLocationRole.LOCATION_SEND).history(history).build();
            HistoryLocation historyLocationReceiver = HistoryLocation.builder().location(locationReceive).role(HistoryLocationRole.LOCATION_RECEIVE).history(history).build();
            List<HistoryLocation> historyLocations = new ArrayList<>();
            historyLocations.add(historyLocationSend);
            historyLocations.add(historyLocationReceiver);
            history.setLocation(historyLocations);

            historyRepository.save(history);
            historyLocationRepository.save(historyLocationSend);
            historyLocationRepository.save(historyLocationReceiver);
            historyUserRepository.save(historySender);
            historyUserRepository.save(historyReceiver);

            EmailDetailDto emailDetailDto = EmailDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .otp(otp.getOtpNumber()).build();

            emailService.sendRegisterLockerMail(emailDetailDto);

            RegisterLockerInfoResp resp = RegisterLockerInfoResp.builder()
                    .lockerId(selectedLocker.getLockerId())
                    .historyId(history.getHistoryId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Register locker successful").data(resp).build();
        }
    }

    @Override
    public OuSmartLockerResp reRegisterConfirm(EmailInfoRequestDto emailInfoRequestDto) {
//        History history = historyRepository.findById(emailInfoRequestDto.getHistoryId()).orElse(null);
//        assert history != null;
//        List<HistoryLocation> historyLocations = history.getLocation();
//        LockerLocation locationSend = null;
//        LockerLocation locationReceive = null;
//        Otp otp = history.getOtp();
//        otp.setExpireTime(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime));
//        otpRepository.save(otp);
//        for (HistoryLocation historyLocation : historyLocations) {
//            if (historyLocation.getRole().equals(HistoryLocationRole.LOCATION_SEND))
//                locationSend = historyLocation.getLocation();
//            else
//                locationReceive = historyLocation.getLocation();
//        }
//        if (Objects.isNull(locationSend) || Objects.isNull(locationReceive))
//            throw new OuSmartLockerInternalErrorException("Something wrong happen");
//        for (User shipper : shippers) {
//            String msgBody = "Hi " + shipper.getName() + ",\n" +
//                    "\n" +
//                    "New order at \n" + locationSend.getLocation() + " to " + locationReceive.getLocation() +
//                    "\n" +
//                    "Please ship this order. Thank you. ";
//            EmailInfoDto emailInfoDto = EmailInfoDto.builder()
//                    .mail(shipper.getEmail())
//                    .content(msgBody)
//                    .subject("New order").build();
//
//            emailService.sendEmail(emailInfoDto);
//        }

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    @Transactional
    public OuSmartLockerResp reRegisterLocker(ReRegisterLockerDto reRegisterLockerDto) {
        History history = historyRepository.findById(reRegisterLockerDto.getHistoryId()).orElse(null);
        if (Objects.isNull(history))
            throw new HistoryInvalidException("Can not find history");
        String userId = readToken.getUserId();
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        HistoryUser historyShipper = HistoryUser.builder().user(user).role(HistoryUserRole.SHIPPER).history(history).build();
        List<HistoryUser> historyUsers = history.getUsers();
        historyUsers.add(historyShipper);
        history.setUsers(historyUsers);
        historyRepository.save(history);
        Locker locker = history.getLocker();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authentication is fail");

        if (Objects.isNull(locker)) {
            return OuSmartLockerResp.builder().status(HttpStatus.NOT_FOUND).message("Not found locker").data("Not found locker").build();
        } else {
            Otp otp = history.getOtp();
            otp.setSetGeneratedAt(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime));
            otp.setExpireTime(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime.plusHours(1)));
            locker.setIsOccupied(true);
            lockerRepository.save(locker);
            EmailDetailDto emailDetailDto = EmailDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .otp(history.getOtp().getOtpNumber()).build();

            emailService.sendRegisterLockerMail(emailDetailDto);

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Re-register locker successful").data("Đăng ký locker thành công").build();
        }
    }

    @Override
    public OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation) {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerLocationRepository.save(lockerLocation)).build();
    }

    @Override
    public OuSmartLockerResp confirmRegisterLockerSuccessful(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        assert history != null;
        List<HistoryLocation> historyLocations = history.getLocation();
        LockerLocation locationSend = null;
        LockerLocation locationReceive = null;
        Otp otp = history.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime));
        otpRepository.save(otp);
        for (HistoryLocation historyLocation : historyLocations) {
            if (historyLocation.getRole().equals(HistoryLocationRole.LOCATION_SEND))
                locationSend = historyLocation.getLocation();
            else
                locationReceive = historyLocation.getLocation();
        }
        if (Objects.isNull(locationSend) || Objects.isNull(locationReceive))
            throw new OuSmartLockerInternalErrorException("Something wrong happen");
        User user = userRepository.findById(Long.valueOf(readToken.getUserId())).orElse(null);
        if (Objects.nonNull(user) && !user.getRoles().contains(Role.ROLE_SHIPPER)) {
            List<User> users = userRepository.findAll();
            List<User> shippers = users.stream().filter(u -> u.getRoles().contains(Role.ROLE_SHIPPER)).toList();
            for (User shipper : shippers) {
                String msgBody = "Hi " + shipper.getName() + ",\n" +
                        "\n" +
                        "New order at \n" + locationSend.getLocation() + " to " + locationReceive.getLocation() +
                        "\n" +
                        "Please ship this order. Thank you. ";
                EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                        .mail(shipper.getEmail())
                        .content(msgBody)
                        .subject("New order").build();

                emailService.sendEmail(emailInfoDto);
            }
        }

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    public OuSmartLockerResp getHistoryById(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(history).build();
    }

    private Otp generateOTP(Locker locker) {
        SecureRandom random = new SecureRandom();
        int otpValue = 1000 + random.nextInt(9000);
        String otp = String.valueOf(otpValue);

        LocalDateTime expireTime = SmartLockerUtils.currentTime.plusMinutes(15);

        Otp otpInfo = Otp.builder()
                .otpNumber(otp)
                .setGeneratedAt(SmartLockerUtils.formatter.format(SmartLockerUtils.currentTime))
                .expireTime(SmartLockerUtils.formatter.format(expireTime))
                .build();

        LockerOtp lockerOtp = LockerOtp.builder().locker(locker).otp(otpInfo).build();

        otpRepository.save(otpInfo);
        lockerOtpRepository.save(lockerOtp);

        return otpInfo;
    }
}
