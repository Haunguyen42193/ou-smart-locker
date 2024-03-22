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
    public OuSmartLockerResp addLocker(Locker locker) {
        locker.setIsOccupied(false);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerRepository.save(locker)).build();
    }

    @Override
    public OuSmartLockerResp getAllLocker() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all locker successful").data(lockerRepository.findAll()).build();
    }

    @Override
    public OuSmartLockerResp getAllLocation() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all locker successful").data(lockerLocationRepository.findAll()).build();
    }

    @Override
    @Transactional
    public OuSmartLockerResp senderRegisterLocker(RegisterLockerDto registerLockerDto) {
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
            Locker selectedLocker = randomLocker(availableLockers);


            Otp otp = this.generateOTP(selectedLocker);
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime localDateTime = currentTime.plusHours(1);
            String endTime = SmartLockerUtils.formatter.format(localDateTime);
            String startTime = SmartLockerUtils.formatter.format(currentTime);
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
    public OuSmartLockerResp confirmSenderRegisterLocker(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        assert history != null;
        List<HistoryLocation> historyLocations = history.getLocation();
        LockerLocation locationSend = null;
        LockerLocation locationReceive = null;
        LocalDateTime currentTime = LocalDateTime.now();
        Otp otp = history.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(currentTime));
        otpRepository.save(otp);
        for (HistoryLocation historyLocation : historyLocations) {
            if (historyLocation.getRole().equals(HistoryLocationRole.LOCATION_SEND))
                locationSend = historyLocation.getLocation();
            else
                locationReceive = historyLocation.getLocation();
        }
        if (Objects.isNull(locationSend) || Objects.isNull(locationReceive))
            throw new OuSmartLockerInternalErrorException("Something wrong happen");
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
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    @Transactional
    public OuSmartLockerResp shipperRegisterLocker(ReRegisterLockerDto reRegisterLockerDto) {
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
        Locker locker = history.getLocker();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authentication is fail");
        LocalDateTime currentTime = LocalDateTime.now();
        if (Objects.isNull(locker)) {
            return OuSmartLockerResp.builder().status(HttpStatus.NOT_FOUND).message("Not found locker").data("Not found locker").build();
        } else {
            Otp otp = this.generateOTP(history.getLocker());
            otp.setSetGeneratedAt(SmartLockerUtils.formatter.format(currentTime));
            otp.setExpireTime(SmartLockerUtils.formatter.format(currentTime.plusHours(1)));
            locker.setIsOccupied(true);
            history.setOtp(otp);
            historyRepository.save(history);
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
    public OuSmartLockerResp confirmShipperRegisterLocker(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);

        assert history != null;
        List<HistoryUser> users = history.getUsers();
        LocalDateTime currentTime = LocalDateTime.now();
        User sender = users.stream().filter(historyUser -> historyUser.getRole().equals(HistoryUserRole.SENDER)).toList().get(0).getUser();
        Otp otp = history.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(currentTime));
        otpRepository.save(otp);

        history.setEndTime(SmartLockerUtils.formatter.format(currentTime));
        historyRepository.save(history);

        Locker locker = history.getLocker();
        locker.setIsOccupied(false);
        lockerRepository.save(locker);

        String msgBody = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order is being shipped\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(sender.getEmail())
                .content(msgBody)
                .subject("New order").build();
        emailService.sendEmail(emailInfoDto);

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Transactional
    public OuSmartLockerResp shipperRegisterSendLocker(ReRegisterLockerDto reRegisterLockerDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        History history = historyRepository.findById(reRegisterLockerDto.getHistoryId()).orElse(null);
        assert history != null;

        List<HistoryLocation> historyLocations = history.getLocation();

        LockerLocation locationReceive = historyLocations.stream()
                .filter(
                        historyLocation -> historyLocation.getRole().equals(HistoryLocationRole.LOCATION_RECEIVE)
                ).findFirst().orElseThrow(() -> new IllegalStateException("Location receive not found")).getLocation();

        List<Locker> availableLockers = lockerRepository.findByIsOccupiedFalseAndLockerLocation(locationReceive.getLocationId());
        Locker selectedLocker = randomLocker(availableLockers);
        if (Objects.isNull(selectedLocker))
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Fail").data("Locker is full").build();

        Otp otp = this.generateOTP(selectedLocker);

        String endTime = SmartLockerUtils.formatter.format(currentTime.plusHours(1));
        String startTime = SmartLockerUtils.formatter.format(currentTime);

        List<HistoryUser> historyUsers = mapHistoryUsersNew(history.getUsers());
        List<HistoryLocation> historyLocationsNew = mapHistoryLocationsNew(historyLocations);
        History historySend = History.builder()
                .locker(selectedLocker)
                .otp(otp)
                .endTime(endTime)
                .startTime(startTime)
                .build();

        for (HistoryLocation historyLocation: historyLocationsNew) {
            historyLocation.setHistory(historySend);
        }

        for (HistoryUser historyUser: historyUsers) {
            historyUser.setHistory(historySend);
        }
        historySend.setUsers(historyUsers);
        historySend.setLocation(historyLocationsNew);

        historyRepository.save(historySend);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    private List<HistoryUser> mapHistoryUsersNew(List<HistoryUser> users) {
        return users.stream().map(this::mapHistoryUserNew).toList();
    }

    private HistoryUser mapHistoryUserNew(HistoryUser historyUser) {
        return HistoryUser.builder()
                .history(historyUser.getHistory())
                .user(historyUser.getUser())
                .role(historyUser.getRole())
                .build();
    }

    private List<HistoryLocation> mapHistoryLocationsNew(List<HistoryLocation> location) {
        return location.stream().map(this::mapHistoryLocationNew).toList();
    }

    private HistoryLocation mapHistoryLocationNew(HistoryLocation historyLocation) {
        return HistoryLocation.builder()
                .history(historyLocation.getHistory())
                .location(historyLocation.getLocation())
                .role(historyLocation.getRole())
                .build();
    }

    public OuSmartLockerResp confirmShipperRegisterSendLocker(Long historyId) {
        LocalDateTime currentTime = LocalDateTime.now();
        History history = historyRepository.findById(historyId).orElse(null);
        assert history != null;

        List<HistoryUser> users = history.getUsers();
        User receiver = users.stream().filter(historyUser -> historyUser.getRole().equals(HistoryUserRole.RECEIVER)).toList().get(0).getUser();
        User sender = users.stream().filter(historyUser -> historyUser.getRole().equals(HistoryUserRole.SENDER)).toList().get(0).getUser();

        List<HistoryLocation> historyLocations = history.getLocation();

        LockerLocation locationReceive = historyLocations.stream()
                .filter(
                        historyLocation -> historyLocation.getRole().equals(HistoryLocationRole.LOCATION_RECEIVE)
                ).toList().get(0).getLocation();

        historyRepository.save(history);
        Otp otp = history.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(currentTime));

        String msgBody = "Hi " + receiver.getName() + ",\n" +
                "\n" +
                "You have an order at " + locationReceive + "\n" +
                "\n" +
                "Please come and take it!\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBody)
                .subject("New order").build();
        emailService.sendEmail(emailInfoDto);

        String msgBodySender = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order shipped to " + locationReceive + "\n";
        EmailInfoDto emailInfoDtoSender = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBodySender)
                .subject("Ship success").build();
        emailService.sendEmail(emailInfoDtoSender);

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    public OuSmartLockerResp receiverRegisterGetLocker(ReRegisterLockerDto reRegisterLockerDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        String userId = readToken.getUserId();
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new AuthorizedFailException("Authorized fail");
        History history = historyRepository.findById(reRegisterLockerDto.getHistoryId()).orElse(null);
        assert history != null;
        Locker selectedLocker = history.getLocker();
        Otp otp = this.generateOTP(selectedLocker);
        history.setOtp(otp);
        historyRepository.save(history);

        EmailDetailDto emailDetailDto = EmailDetailDto.builder()
                .name(user.getName())
                .mail(user.getEmail())
                .otp(history.getOtp().getOtpNumber()).build();

        emailService.sendRegisterLockerMail(emailDetailDto);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").build();
    }

    @Override
    public OuSmartLockerResp confirmReceiverRegisterSendLocker(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        assert history != null;
        String userId = readToken.getUserId();
        User receiver = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(receiver))
            throw new AuthorizedFailException("Authorized fail");
        List<HistoryUser> users = history.getUsers();
        User sender = users.stream().filter(historyUser -> historyUser.getRole().equals(HistoryUserRole.SENDER)).toList().get(0).getUser();

        String msgBody = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order had been sent to " + receiver.getName() + "\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBody)
                .subject("New order").build();
        emailService.sendEmail(emailInfoDto);

        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").build();
    }

    @Override
    public OuSmartLockerResp getAllHistory() {
        List<History> histories = historyRepository.findAll();
        List<HistoryDto> historyDtos = histories.stream().map(this::mapHistoryToDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDtos).build();
    }

    @Override
    public OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation) {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerLocationRepository.save(lockerLocation)).build();
    }


    @Override
    public OuSmartLockerResp getHistoryById(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        if (Objects.isNull(history))
            throw new HistoryNotFoundException("Not found history");
        HistoryDto historyDto = mapHistoryToDto(history);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDto).build();
    }

    private HistoryDto mapHistoryToDto(History history) {
        return HistoryDto.builder()
                .historyId(history.getHistoryId())
                .locker(history.getLocker())
                .endTime(history.getEndTime())
                .startTime(history.getStartTime())
                .location(history.getLocation().stream().map(this::mapHistoryLocationToDto).toList())
                .users(history.getUsers().stream().map(this::mapHistoryUserToDto).toList())
                .otp(history.getOtp())
                .build();
    }

    private HistoryUserDto mapHistoryUserToDto(HistoryUser historyUser) {
        return HistoryUserDto.builder()
                .user(historyUser.getUser())
                .role(historyUser.getRole())
                .build();
    }

    private HistoryLocationDto mapHistoryLocationToDto(HistoryLocation historyLocation) {
        return HistoryLocationDto.builder()
                .location(historyLocation.getLocation())
                .role(historyLocation.getRole())
                .build();
    }

    private Otp generateOTP(Locker locker) {
        SecureRandom random = new SecureRandom();
        int otpValue = 1000 + random.nextInt(9000);
        String otp = String.valueOf(otpValue);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = currentTime.plusMinutes(15);

        Otp otpInfo = Otp.builder()
                .otpNumber(otp)
                .setGeneratedAt(SmartLockerUtils.formatter.format(currentTime))
                .expireTime(SmartLockerUtils.formatter.format(expireTime))
                .build();

        LockerOtp lockerOtp = LockerOtp.builder().locker(locker).otp(otpInfo).build();

        otpRepository.save(otpInfo);
        lockerOtpRepository.save(lockerOtp);

        return otpInfo;
    }

    private Locker randomLocker(List<Locker> availableLockers) {
        SecureRandom random = new SecureRandom();
        if (availableLockers.size() == 0)
            return null;
        int randomIndex = random.nextInt(availableLockers.size());
        Locker selectedLocker = availableLockers.get(randomIndex);
        selectedLocker.setIsOccupied(true);
        lockerRepository.save(selectedLocker);
        return selectedLocker;
    }
}
