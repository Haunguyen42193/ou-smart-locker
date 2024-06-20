package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.UserDto;
import com.example.ousmartlocker.dto.ChangePassDto;
import com.example.ousmartlocker.dto.ForgotPasswordRequest;
import com.example.ousmartlocker.dto.SenderDetailDto;
import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.SenderPasswordDto;
import com.example.ousmartlocker.dto.UpdateUserInfoDto;
import com.example.ousmartlocker.dto.LoginRecordDto;
import com.example.ousmartlocker.dto.LoginRecordUserDto;
import com.example.ousmartlocker.dto.mapper.ModelMapper;
import com.example.ousmartlocker.exception.UserNotFoundException;
import com.example.ousmartlocker.exception.RoleAlreadyExistsException;
import com.example.ousmartlocker.exception.TokenInvalidException;
import com.example.ousmartlocker.exception.PasswordInvalidException;
import com.example.ousmartlocker.exception.RequestDataIsNullException;
import com.example.ousmartlocker.exception.OtpInvalidException;
import com.example.ousmartlocker.exception.OuSmartLockerInternalErrorException;
import com.example.ousmartlocker.exception.InvalidTimeException;
import com.example.ousmartlocker.model.PassResetOtp;
import com.example.ousmartlocker.model.User;
import com.example.ousmartlocker.model.enums.Role;
import com.example.ousmartlocker.repository.LoginRecordRepository;
import com.example.ousmartlocker.repository.PassResetOtpRepository;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.services.SenderService;
import com.example.ousmartlocker.services.UserService;
import com.example.ousmartlocker.util.PasswordUtils;
import com.example.ousmartlocker.util.ReadToken;
import com.example.ousmartlocker.util.SmartLockerUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ReadToken readToken;

    private final PasswordEncoder passwordEncoder;

    private final PassResetOtpRepository passResetOtpRepository;

    private final SenderService senderService;

    private final LoginRecordRepository loginRecordRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ReadToken readToken, PasswordEncoder passwordEncoder, PassResetOtpRepository passResetOtpRepository, SenderService senderService, LoginRecordRepository loginRecordRepository) {
        this.userRepository = userRepository;
        this.readToken = readToken;
        this.passwordEncoder = passwordEncoder;
        this.passResetOtpRepository = passResetOtpRepository;
        this.senderService = senderService;
        this.loginRecordRepository = loginRecordRepository;
    }

    @Override
    public OuSmartLockerResp getAllUser() {
        List<UserDto> userDto = userRepository.findAll().stream().map(ModelMapper::convertUserToUserDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all user").data(userDto).build();
    }

    @Override
    public OuSmartLockerResp addRole(Long id, int role) {
        User user = userRepository.findById(id).orElse(null);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }
        Role roles = checkRole(role, user);
        user.getRoles().add(roles);
        userRepository.save(user);
        UserDto userDto = ModelMapper.convertUserToUserDto(user);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add role success").data(userDto).build();
    }

    @Override
    public OuSmartLockerResp changePassword(ChangePassDto changePassDto) {
        String uuid = readToken.getUserId();
        if (Strings.isBlank(uuid)) {
            throw new TokenInvalidException("Authorized is fail");
        }
        User user = userRepository.findById(Long.valueOf(uuid)).orElseThrow();
        if (passwordEncoder.matches(user.getPassword(), changePassDto.getOldPass())) {
            throw new PasswordInvalidException("Password invalid!!");
        }
        user.setPassword(passwordEncoder.encode(changePassDto.getNewPass()));
        userRepository.save(user);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Change password success!").build();
    }

    @Override
    public OuSmartLockerResp forgottenPass(ForgotPasswordRequest request) {
        if (Strings.isBlank(request.getMail())) {
            throw new RequestDataIsNullException("Mail is not valid");
        }
        User user = userRepository.findByEmail(request.getMail()).orElse(null);
        if (Objects.isNull(user)) {
            throw new RequestDataIsNullException("Mail is not registered");
        }
        PassResetOtp otp = generateResetPassOtp(user);
        SenderDetailDto senderDetailDto = SenderDetailDto.builder()
                .name(user.getName())
                .mail(user.getEmail())
                .phone(user.getPhone())
                .otp(otp.getOtp())
                .build();
        senderService.sendPasswordResetMail(senderDetailDto);
        return OuSmartLockerResp.builder()
                .status(HttpStatus.OK)
                .message("Request successful")
                .build();
    }

    @Override
    public OuSmartLockerResp confirm(EmailInfoRequestDto requestDto) {
        PassResetOtp passResetOtp = passResetOtpRepository.findByOtp(requestDto.getOtp());
        if (Objects.isNull(passResetOtp)) {
            throw new OtpInvalidException("Otp incorrect");
        }
        SmartLockerUtils.validateExpireTime(passResetOtp.getExpireTime());
        String newPassword = PasswordUtils.generatePassword();
        User user = passResetOtp.getUser();
        if (Objects.isNull(user))
            throw new OuSmartLockerInternalErrorException("Something wrong. Try again");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        SenderPasswordDto passwordDto = SenderPasswordDto.builder()
                .mail(user.getEmail())
                .phone(user.getPhone())
                .name(user.getName())
                .newPass(newPassword)
                .build();
        LocalDateTime currentTime = LocalDateTime.now();
        passResetOtp.setExpireTime(SmartLockerUtils.formatter.format(currentTime));
        passResetOtpRepository.save(passResetOtp);
        senderService.sendNewPassword(passwordDto);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Confirm success").build();
    }

    @Override
    public OuSmartLockerResp updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        String userId = readToken.getUserId();
        if (Strings.isBlank(userId)) {
            throw new TokenInvalidException("Authorized is fail");
        }
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found!");
        }
        if (!Strings.isBlank(updateUserInfoDto.getPhone())) {
            user.setPhone(updateUserInfoDto.getPhone());
        }
        if (!Strings.isBlank(updateUserInfoDto.getName())) {
            user.setName(updateUserInfoDto.getName());
        }
        if (!Strings.isBlank(updateUserInfoDto.getMail())) {
            user.setEmail(updateUserInfoDto.getMail());
        }
        userRepository.saveAndFlush(user);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Update user info successfully!").data(user).build();
    }

    @Override
    public OuSmartLockerResp getRecordLogin(String startDate, String endDate) {
        if (Strings.isBlank(startDate)) {
            throw new InvalidTimeException("Start time is invalid");
        }
        LocalDate end;
        Date dateTimeEnd;
        if (Strings.isBlank(endDate)) {
            end = LocalDate.now();
            dateTimeEnd = Timestamp.valueOf(end.atStartOfDay());
        } else {
            end = LocalDate.parse(endDate, SmartLockerUtils.timeFormatter);
            dateTimeEnd = Timestamp.valueOf(end.atStartOfDay());
        }
        LocalDate start = LocalDate.parse(startDate, SmartLockerUtils.timeFormatter);
        Date dateTimeStart = Timestamp.valueOf(start.atStartOfDay());
        LoginRecordDto loginRecordDto = new LoginRecordDto();
        long loginRecord = loginRecordRepository.countLoginRecordsBetween(dateTimeStart, dateTimeEnd);
        loginRecordDto.setRecord(loginRecord);
        List<LoginRecordUserDto> loginRecordUserDtos = loginRecordRepository.countLoginsByUsername(dateTimeStart, dateTimeEnd);
        loginRecordDto.setLoginRecordUserDtos(loginRecordUserDtos);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).data(loginRecordDto).message("Get success").build();
    }

    @Override
    public OuSmartLockerResp removeRole(Long id, int role) {
        User user = userRepository.findById(id).orElse(null);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }
        Role roles = checkRole(role, user);
        user.getRoles().remove(roles);
        userRepository.save(user);
        UserDto userDto = ModelMapper.convertUserToUserDto(user);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Remove role success").data(userDto).build();
    }

    private PassResetOtp generateResetPassOtp(User user) {
        SecureRandom random = new SecureRandom();
        int randValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(randValue);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = currentTime.plusMinutes(15);

        PassResetOtp passResetOtp = PassResetOtp.builder()
                .otp(otp)
                .user(user)
                .setGeneratedAt(SmartLockerUtils.formatter.format(currentTime))
                .expireTime(SmartLockerUtils.formatter.format(expireTime))
                .build();
        passResetOtpRepository.save(passResetOtp);
        return passResetOtp;
    }

    private Role checkRole(int role, User user) {
        Role roles;
        switch (role) {
            case 1:
                roles = Role.ROLE_ADMIN;
                if (user.getRoles().contains(Role.ROLE_ADMIN))
                    throw new RoleAlreadyExistsException("Add role fail. This user had role admin");
                break;
            case 3:
                roles = Role.ROLE_SHIPPER;
                if (user.getRoles().contains(Role.ROLE_SHIPPER))
                    throw new RoleAlreadyExistsException("Add role fail. This user had role shipper");
                break;
            default:
                roles = Role.ROLE_USER;
                if (user.getRoles().contains(Role.ROLE_USER))
                    throw new RoleAlreadyExistsException("Add role fail. This user had role user");
                break;
        }
        return roles;
    }
}
