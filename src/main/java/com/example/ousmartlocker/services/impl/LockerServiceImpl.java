package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.dto.mapper.ModelMapper;
import com.example.ousmartlocker.exception.*;
import com.example.ousmartlocker.model.*;
import com.example.ousmartlocker.model.enums.HistoryLocationRole;
import com.example.ousmartlocker.model.enums.HistoryUserRole;
import com.example.ousmartlocker.model.enums.Role;
import com.example.ousmartlocker.repository.*;
import com.example.ousmartlocker.services.SenderService;
import com.example.ousmartlocker.services.LockerService;
import com.example.ousmartlocker.services.SmsService;
import com.example.ousmartlocker.util.ReadToken;
import com.example.ousmartlocker.util.SmartLockerUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LockerServiceImpl implements LockerService {
    private final LockerRepository lockerRepository;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final LockerLocationRepository lockerLocationRepository;
    private final HistoryRepository historyRepository;
    private final HistoryUserRepository historyUserRepository;
    private final HistoryLocationRepository historyLocationRepository;
    private final LockerOtpRepository lockerOtpRepository;
    private final SenderService senderService;
    private final ReadToken readToken;
    private final SmsService smsService;
    private final LockerUsingRecordRepository lockerUsingRecordRepository;

    @Autowired
    public LockerServiceImpl(LockerRepository lockerRepository, UserRepository userRepository, OtpRepository otpRepository, LockerLocationRepository lockerLocationRepository, HistoryRepository historyRepository, HistoryUserRepository historyUserRepository, HistoryLocationRepository historyLocationRepository, LockerOtpRepository lockerOtpRepository, SenderService senderService, ReadToken readToken, SmsService smsService, LockerUsingRecordRepository lockerUsingRecordRepository) {
        this.lockerRepository = lockerRepository;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.lockerLocationRepository = lockerLocationRepository;
        this.historyRepository = historyRepository;
        this.historyUserRepository = historyUserRepository;
        this.historyLocationRepository = historyLocationRepository;
        this.lockerOtpRepository = lockerOtpRepository;
        this.senderService = senderService;
        this.readToken = readToken;
        this.smsService = smsService;
        this.lockerUsingRecordRepository = lockerUsingRecordRepository;
    }

    @Override
    public OuSmartLockerResp addLocker(Locker locker) {
        locker.setIsOccupied(false);
        lockerRepository.save(locker);
        LockerDto lockerDto = ModelMapper.mapToLockerDto(locker);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(lockerDto).build();
    }

    @Override
    public OuSmartLockerResp getAllLocker() {
        List<Locker> lockers = lockerRepository.findAll();
        List<LockerDto> lockerDtos = lockers.stream().map(ModelMapper::mapToLockerDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all locker successful").data(lockerDtos).build();
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
            List<Locker> availableLockersReceive = lockerRepository.findByIsOccupiedFalseAndLockerLocation(registerLockerDto.getLocationReceive());
            if (availableLockersReceive.isEmpty()) {
                return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Register locker fail").data("Không còn locker trống hoặc đăng ký không thành công").build();
            }
            SmartLockerUtils.validatePhoneNumber(user.getPhone());
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

            SenderDetailDto senderDetailDto = SenderDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .phone(user.getPhone())
                    .otp(otp.getOtpNumber()).build();

            senderService.sendRegisterLockerMail(senderDetailDto);

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
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
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
        saveLockerUsingRecord(user, history.getLocker());
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

            senderService.sendEmail(emailInfoDto);
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
            SenderDetailDto senderDetailDto = SenderDetailDto.builder()
                    .name(user.getName())
                    .mail(user.getEmail())
                    .phone(user.getPhone())
                    .otp(history.getOtp().getOtpNumber()).build();

            senderService.sendRegisterLockerMail(senderDetailDto);

            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Re-register locker successful").data("Đăng ký locker thành công").build();
        }
    }

    @Override
    public OuSmartLockerResp confirmShipperRegisterLocker(Long historyId) {
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
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

        saveLockerUsingRecord(user, history.getLocker());

        String msgBody = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order is being shipped\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(sender.getEmail())
                .content(msgBody)
                .subject("Order shipped").build();
        senderService.sendEmail(emailInfoDto);
        smsService.sendSms(sender.getPhone(), msgBody);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Transactional
    public OuSmartLockerResp shipperRegisterSendLocker(ReRegisterLockerDto reRegisterLockerDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");

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

        List<HistoryUser> historyUsers = ModelMapper.mapHistoryUsersNew(history.getUsers());
        List<HistoryLocation> historyLocationsNew = ModelMapper.mapHistoryLocationsNew(historyLocations);
        History historySend = History.builder()
                .locker(selectedLocker)
                .otp(otp)
                .endTime(endTime)
                .startTime(startTime)
                .build();

        for (HistoryLocation historyLocation : historyLocationsNew) {
            historyLocation.setHistory(historySend);
        }

        for (HistoryUser historyUser : historyUsers) {
            historyUser.setHistory(historySend);
        }
        historySend.setUsers(historyUsers);
        historySend.setLocation(historyLocationsNew);
        historySend.setOnProcedure(history.getHistoryId());

        historyRepository.save(historySend);
        history.setOnProcedure(historySend.getHistoryId());
        historyRepository.save(history);

        SenderDetailDto senderDetailDto = SenderDetailDto.builder()
                .name(user.getName())
                .mail(user.getEmail())
                .phone(user.getPhone())
                .otp(otp.getOtpNumber()).build();

        senderService.sendRegisterLockerMail(senderDetailDto);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    public OuSmartLockerResp confirmShipperRegisterSendLocker(Long historyId) {
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
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
        otpRepository.save(otp);

        saveLockerUsingRecord(user, history.getLocker());

        String msgBody = "Hi " + receiver.getName() + ",\n" +
                "\n" +
                "You have an order at " + locationReceive.getLocation() + "\n" +
                "\n" +
                "Please come and take it!\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBody)
                .subject("New order").build();
        senderService.sendEmail(emailInfoDto);
        String msgBodySender = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order shipped to " + locationReceive.getLocation() + "\n";
        EmailInfoDto emailInfoDtoSender = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBodySender)
                .subject("Ship success").build();
        senderService.sendEmail(emailInfoDtoSender);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data("Shipper get information of this order").build();
    }

    @Override
    public OuSmartLockerResp receiverRegisterGetLocker(ReRegisterLockerDto reRegisterLockerDto) {

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

        SenderDetailDto senderDetailDto = SenderDetailDto.builder()
                .name(user.getName())
                .mail(user.getEmail())
                .phone(user.getPhone())
                .otp(history.getOtp().getOtpNumber()).build();

        senderService.sendRegisterLockerMail(senderDetailDto);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").build();
    }

    @Override
    public OuSmartLockerResp confirmReceiverRegisterSendLocker(Long historyId) {
        LocalDateTime currentTime = LocalDateTime.now();

        History history = historyRepository.findById(historyId).orElse(null);
        assert history != null;
        history.setEndTime(SmartLockerUtils.formatter.format(LocalDateTime.now()));
        historyRepository.save(history);
        Locker locker = history.getLocker();
        locker.setIsOccupied(false);
        lockerRepository.save(locker);
        String userId = readToken.getUserId();
        User receiver = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(receiver))
            throw new AuthorizedFailException("Authorized fail");
        List<HistoryUser> users = history.getUsers();
        User sender = users.stream().filter(historyUser -> historyUser.getRole().equals(HistoryUserRole.SENDER)).toList().get(0).getUser();
        Otp otp = history.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(currentTime));
        otpRepository.save(otp);

        saveLockerUsingRecord(receiver, history.getLocker());

        String msgBody = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order had been sent to " + receiver.getName() + "\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(receiver.getEmail())
                .content(msgBody)
                .subject("New order").build();
        senderService.sendEmail(emailInfoDto);
        smsService.sendSms(sender.getPhone(), msgBody);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").build();
    }

    @Override
    public OuSmartLockerResp shipperConfirmOrderLocker(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
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
        User sender = historyUsers.stream().filter(historyUser -> historyUser.getRole() == HistoryUserRole.SENDER).findFirst().get().getUser();
        String msgBody = "Hi " + sender.getName() + ",\n" +
                "\n" +
                "Your order has been confirmed by " + historyShipper.getUser().getName() + "\n";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(sender.getEmail())
                .content(msgBody)
                .subject("Order confirmed").build();
        senderService.sendEmail(emailInfoDto);
        smsService.sendSms(sender.getPhone(), msgBody);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").build();
    }

    @Override
    public OuSmartLockerResp verifyAndOpenLocker(OpenLockerRequestDto request) {
        List<Otp> otps = otpRepository.findByOtpNumber(request.getOtp());
        if (otps.isEmpty())
            throw new OtpInvalidException("Not found otp");
        Otp otpRequest = null;
        for (Otp otp : otps) {
            SmartLockerUtils.validateExpireTime(otp.getExpireTime());
            otpRequest = otp;
            if (!Objects.isNull(otpRequest))
                break;
        }
        Optional<LockerOtp> lockerOtpOptional = lockerOtpRepository.findByOtp(otpRequest);
        if (!lockerOtpOptional.isPresent()) {
            return OuSmartLockerResp.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Locker OTP not found")
                    .build();
        }

        LockerOtp lockerOtp = lockerOtpOptional.get();
        Otp otp = lockerOtp.getOtp();
        otp.setExpireTime(SmartLockerUtils.formatter.format(LocalDateTime.now()));
        otpRepository.save(otp);
        if (!otp.getOtpNumber().equals(request.getOtp())) {
            return OuSmartLockerResp.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid or expired OTP")
                    .build();
        }

        Locker locker = lockerOtp.getLocker();
        LockerDto lockerDto = ModelMapper.mapToLockerDto(locker);
        return OuSmartLockerResp.builder()
                .status(HttpStatus.OK)
                .message("Locker opened successfully")
                .data(lockerDto)
                .build();
    }

    @Override
    public OuSmartLockerResp registerRetry(ReRegisterLockerDto reRegisterLockerDto) {
        History history = historyRepository.findById(reRegisterLockerDto.getHistoryId()).orElse(null);
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId))
            throw new TokenInvalidException("Authorized is fail");
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found!");
        if (Objects.isNull(history))
            throw new HistoryNotFoundException("Not found history");
        Otp otpNew = generateOTP(history.getLocker());
        history.setOtp(otpNew);
        historyRepository.save(history);
        String msgBody = "Hi " + user.getName() + ",\n" +
                "\n" +
                "Your OTP is " + otpNew.getOtpNumber() + ".\n" +
                "\n" +
                "Using this for unlocked SmartLocker\n" +
                "\n" +
                "Contact us: 0987654321";
        EmailInfoDto emailInfoDto = EmailInfoDto.builder()
                .mail(user.getEmail())
                .content(msgBody)
                .subject("New order").build();
        senderService.sendEmail(emailInfoDto);
        smsService.sendSms(user.getPhone(), msgBody);
        OtpDto otpDto = ModelMapper.mapToOtpDto(otpNew);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add locker successful").data(otpDto).build();
    }

    @Override
    public OuSmartLockerResp deleteLocker(long id) {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(() -> new LockerNotFoundException("Locker not found"));
        lockerRepository.delete(locker);
        return OuSmartLockerResp.builder().status(HttpStatus.NO_CONTENT).message("Success").build();
    }

    @Override
    public OuSmartLockerResp updateLocker(long id, LockerDto dto) {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(() -> new LockerNotFoundException("Locker not found"));
        locker.setIsOccupied(dto.getIsOccupied());
        locker.setLockerName(dto.getLockerName());
        locker.setLockerLocation(ModelMapper.mapLockerLocation(dto.getLockerLocation()));
        lockerRepository.save(locker);
        LockerDto lockerDto = ModelMapper.mapToLockerDto(locker);
        return OuSmartLockerResp.builder()
                .status(HttpStatus.OK)
                .message("Success")
                .data(lockerDto)
                .build();
    }

    @Override
    public OuSmartLockerResp countLocker() {
        long num = lockerRepository.count();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Count locker success").data(num).build();
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

    private LockerUsingRecord saveLockerUsingRecord(User user, Locker locker) {
        LockerUsingRecord lockerUsingRecord = LockerUsingRecord.builder()
                .user(user)
                .locker(locker)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        lockerUsingRecordRepository.save(lockerUsingRecord);
        return lockerUsingRecord;
    }
}
