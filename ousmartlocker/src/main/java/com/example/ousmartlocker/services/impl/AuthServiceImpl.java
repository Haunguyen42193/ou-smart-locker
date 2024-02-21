package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.entity.Role;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.exception.PasswordInvalidExceptionBadRequest;
import com.example.ousmartlocker.exception.RequestDataIsNullExceptionBadRequest;
import com.example.ousmartlocker.exception.UserAlreadyExistsExceptionBadRequest;
import com.example.ousmartlocker.model.AuthResponseModel;
import com.example.ousmartlocker.model.ConvertData;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.payload.LoginDto;
import com.example.ousmartlocker.payload.SignUpDto;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.security.JwtTokenProvider;
import com.example.ousmartlocker.services.AuthService;
import com.example.ousmartlocker.util.PasswordValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.jwt-expiration-milliseconds}")
    private Long expiration;

    @Override
    public OuSmartLockerResp authenticate(LoginDto signUpDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signUpDto.getUsernameOrEmail(),
                    signUpDto.getPassword()
            ));
            User user = userRepository.findByUsername(signUpDto.getUsernameOrEmail());
            if (user.getUsername() != null) {
                String token = tokenProvider.generateToken(authentication);
                AuthResponseModel responseModel = AuthResponseModel.builder().accessToken(token).user(ConvertData.convertUserToUserDto(user)).loginTime(System.currentTimeMillis()).expirationDuration(expiration).build();
                return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Sucessfully logged in").data(responseModel).build();
            } else {
                throw new OuSmartLockerBadRequestApiException("User not found");
            }
        } catch (AuthenticationException ex) {
            throw new OuSmartLockerBadRequestApiException("Username or password are incorrect", ex.getCause());
        }
    }

    @Override
    public OuSmartLockerResp createUser(SignUpDto signUpDto) {
        if (Objects.isNull(signUpDto)) {
            throw new RequestDataIsNullExceptionBadRequest("Sign up fail. Sign up data is null");
        }
        boolean usernameIsExists = userRepository.existsByUsername(signUpDto.getUsername());
        boolean emailIsExists = userRepository.existsByEmail(signUpDto.getEmail());
        if (usernameIsExists || emailIsExists)
            throw new UserAlreadyExistsExceptionBadRequest("Sign up fail. User already exists");
        if (!PasswordValidate.validatePassword(signUpDto.getPassword()))
            throw new PasswordInvalidExceptionBadRequest("Sign up fail. Password invalid");
        User user = convertSignUpDtoToUser(signUpDto);
        userRepository.save(user);
        String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                signUpDto.getPassword()
        ));
        AuthResponseModel responseModel = AuthResponseModel.builder().accessToken(token).user(ConvertData.convertUserToUserDto(user)).loginTime(System.currentTimeMillis()).expirationDuration(expiration).build();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Sign up successful").data(responseModel).build();
    }

    private User convertSignUpDtoToUser(SignUpDto signUpDto) {
        return User.builder()
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }
}
