package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.LoginDto;
import com.example.ousmartlocker.dto.ConvertData;
import com.example.ousmartlocker.dto.SignUpDto;
import com.example.ousmartlocker.dto.AuthResponseDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.exception.UsernamePasswordInvalid;
import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.exception.RequestDataIsNullException;
import com.example.ousmartlocker.exception.UserAlreadyExistsException;
import com.example.ousmartlocker.exception.PasswordInvalidException;
import com.example.ousmartlocker.exception.EmailInvalidException;
import com.example.ousmartlocker.model.LoginRecord;
import com.example.ousmartlocker.model.User;
import com.example.ousmartlocker.model.enums.Role;
import com.example.ousmartlocker.repository.LoginRecordRepository;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.security.JwtTokenProvider;
import com.example.ousmartlocker.services.AuthService;
import com.example.ousmartlocker.util.PasswordUtils;
import com.example.ousmartlocker.util.SmartLockerUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtTokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final LoginRecordRepository loginRecordRepository;


    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtTokenProvider tokenProvider,
            PasswordEncoder passwordEncoder,
            LoginRecordRepository loginRecordRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.loginRecordRepository = loginRecordRepository;
    }

    @Value("${app.jwt-expiration-milliseconds}")
    private Long expiration;

    @Override
    public OuSmartLockerResp authenticate(LoginDto loginDto) {
        try {
            if (Strings.isBlank(loginDto.getUsernameOrEmail()) || Strings.isBlank(loginDto.getPassword()))
                throw new UsernamePasswordInvalid("Username or password invalid!");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(),
                    loginDto.getPassword()
            ));
            User user = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
            if (Objects.isNull(user)) {
                throw new OuSmartLockerBadRequestApiException("User not found");
            }
            String token = tokenProvider.generateToken(authentication);
            AuthResponseDto responseModel = AuthResponseDto.builder().accessToken(token).user(ConvertData.convertUserToUserDto(user)).loginTime(System.currentTimeMillis()).expirationDuration(expiration).build();
            LoginRecord loginRecord = LoginRecord.builder().username(user.getUsername()).loginTime(Timestamp.valueOf(LocalDateTime.now())).build();
            loginRecordRepository.save(loginRecord);
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Sucessfully logged in").data(responseModel).build();
        } catch (AuthenticationException ex) {
            throw new OuSmartLockerBadRequestApiException("Username or password are incorrect", ex.getCause());
        }
    }

    @Override
    public OuSmartLockerResp createUser(SignUpDto signUpDto) {
        if (Objects.isNull(signUpDto)) {
            throw new RequestDataIsNullException("Sign up fail. Sign up data is null");
        }
        boolean usernameIsExists = userRepository.existsByUsername(signUpDto.getUsername());
        boolean emailIsExists = userRepository.existsByEmail(signUpDto.getEmail());
        if (usernameIsExists || emailIsExists)
            throw new UserAlreadyExistsException("Sign up fail. User already exists");
        if (!PasswordUtils.validatePassword(signUpDto.getPassword()))
            throw new PasswordInvalidException("Sign up fail. Password invalid");
        if (!SmartLockerUtils.validateEmail(signUpDto.getEmail()))
            throw new EmailInvalidException("Sign up fail. Email invalid");
        User user = convertSignUpDtoToUser(signUpDto);
        userRepository.save(user);
        String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                signUpDto.getPassword()
        ));
        AuthResponseDto responseModel = AuthResponseDto.builder().accessToken(token).user(ConvertData.convertUserToUserDto(user)).loginTime(System.currentTimeMillis()).expirationDuration(expiration).build();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Sign up successful").data(responseModel).build();
    }

    private User convertSignUpDtoToUser(SignUpDto signUpDto) {
        return User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword().trim()))
                .phone(SmartLockerUtils.formatPhoneNumber(signUpDto.getPhone()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }
}
