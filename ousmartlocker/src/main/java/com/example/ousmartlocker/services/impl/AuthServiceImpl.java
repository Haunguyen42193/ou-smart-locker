package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.entity.Role;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.exception.*;
import com.example.ousmartlocker.model.AuthResponseModel;
import com.example.ousmartlocker.model.ConvertData;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.payload.LoginDto;
import com.example.ousmartlocker.payload.SignUpDto;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.security.JwtTokenProvider;
import com.example.ousmartlocker.services.AuthService;
import com.example.ousmartlocker.util.PasswordValidate;
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
    public OuSmartLockerResp authenticate(LoginDto loginDto) {
        try {
            if (Strings.isBlank(loginDto.getUsernameOrEmail()) || Strings.isBlank(loginDto.getPassword()))
                throw new UsernamePasswordInvalid("Username or password invalid!");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(),
                    loginDto.getPassword()
            ));
            User user = userRepository.findByUsername(loginDto.getUsernameOrEmail());
            if (Objects.isNull(user)) {
                throw new OuSmartLockerBadRequestApiException("User not found");
            }
            String token = tokenProvider.generateToken(authentication);
            AuthResponseModel responseModel = AuthResponseModel.builder().accessToken(token).user(ConvertData.convertUserToUserDto(user)).loginTime(System.currentTimeMillis()).expirationDuration(expiration).build();
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
        if (!PasswordValidate.validatePassword(signUpDto.getPassword()))
            throw new PasswordInvalidException("Sign up fail. Password invalid");
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
                .password(passwordEncoder.encode(signUpDto.getPassword().trim()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }
}
