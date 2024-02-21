package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.constant.AuthServiceTestData;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.payload.LoginDto;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.util.ConvertDataUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootTest
class AuthServiceImplTest {
    @InjectMocks
    @Autowired
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void authenticate() throws JsonProcessingException {
        User user = ConvertDataUtil.convertDataToObject(AuthServiceTestData.mockUserData, User.class);
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("johndoe");
        loginDto.setPassword("123456@Aa");
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepository.findByUsernameOrEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), loginDto.getPassword());
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        OuSmartLockerResp resp = authService.authenticate(loginDto);
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatus());
        Assertions.assertNotNull(resp.getData());
        Assertions.assertEquals("Sucessfully logged in", resp.getMessage());
    }

    @Test
    void createUser() {
    }
}