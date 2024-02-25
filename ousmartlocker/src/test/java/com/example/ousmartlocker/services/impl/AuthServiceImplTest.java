package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.constant.AuthServiceTestData;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.exception.UsernamePasswordInvalid;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.payload.LoginDto;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.security.JwtTokenProvider;
import com.example.ousmartlocker.util.ConvertDataUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootTest
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void authenticate() throws JsonProcessingException {
        User user = ConvertDataUtil.convertDataToObject(AuthServiceTestData.mockUserData, User.class);
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("johndoe");
        loginDto.setPassword("123456@Aa");
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJoYXUxMjM0IiwidXNlcklkIjo0LCJpYXQiOjE3MDg1NjkyNjQsImV4cCI6MTcwODU3NjQ2NH0.nP8igrTYjWyOEimTuxCdle3ebsHpnWBfX0CEjBWn8vMn9kkW8JaCrtqr2Txr2ZTT";
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), loginDto.getPassword());
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(tokenProvider.generateToken(authentication)).thenReturn(token);
        OuSmartLockerResp resp = authService.authenticate(loginDto);
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatus());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertEquals("Sucessfully logged in", resp.getMessage());
    }

    @Test
    void authenticate_Error_Input() throws JsonProcessingException {
        User user = ConvertDataUtil.convertDataToObject(AuthServiceTestData.mockUserData, User.class);
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("");
        loginDto.setPassword("123456@Aa");
        Assertions.assertThrows(UsernamePasswordInvalid.class, () -> authService.authenticate(loginDto));
        loginDto.setPassword("");
        loginDto.setUsernameOrEmail("johndoe");
        Assertions.assertThrows(UsernamePasswordInvalid.class, () -> authService.authenticate(loginDto));
    }

    @Test
    void authenticate_Error_Exception() throws JsonProcessingException {
        User user = ConvertDataUtil.convertDataToObject(AuthServiceTestData.mockUserData, User.class);
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("");
        loginDto.setPassword("123456@Aa");
        Assertions.assertThrows(UsernamePasswordInvalid.class, () -> authService.authenticate(loginDto));

        loginDto.setPassword("");
        loginDto.setUsernameOrEmail("johndoe");
        Assertions.assertThrows(UsernamePasswordInvalid.class, () -> authService.authenticate(loginDto));

        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new OuSmartLockerBadRequestApiException("Username or password are incorrect"));
        loginDto.setPassword("123");
        Assertions.assertThrows(OuSmartLockerBadRequestApiException.class, () -> authService.authenticate(loginDto));

        loginDto.setUsernameOrEmail("john2doe");
        loginDto.setPassword("123456@Aa");
        Assertions.assertThrows(OuSmartLockerBadRequestApiException.class, () -> authService.authenticate(loginDto));

    }

    @Test
    void createUser() {

    }
}