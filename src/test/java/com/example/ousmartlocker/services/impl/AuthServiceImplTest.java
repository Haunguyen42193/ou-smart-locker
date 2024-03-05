package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.constant.AuthServiceTestData;
import com.example.ousmartlocker.dto.LoginDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.SignUpDto;
import com.example.ousmartlocker.exception.OuSmartLockerBadRequestApiException;
import com.example.ousmartlocker.exception.UsernamePasswordInvalid;
import com.example.ousmartlocker.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

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
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("HA");
        signUpDto.setUsername("ha12");
        signUpDto.setEmail("email@gmail.com");
        signUpDto.setPassword("12345Aa@");
        signUpDto.setPhone("1234567890");
        // Mocking userRepository.existsByUsername() and userRepository.existsByEmail() to return false
        Mockito.when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);

        // Mocking tokenProvider.generateToken() to return a token
        Mockito.when(tokenProvider.generateToken(Mockito.any())).thenReturn("token");

        // Mocking userRepository.save() to return the user passed to it
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("abcdef");

        // Call the method under test
        OuSmartLockerResp response = authService.createUser(signUpDto);

        // Verify that userRepository.save() was called with the correct user
        Mockito.verify(userRepository).save(Mockito.argThat(user -> user.getUsername().equals(signUpDto.getUsername())));

        // Verify that tokenProvider.generateToken() was called with the correct parameters
        Mockito.verify(tokenProvider).generateToken(Mockito.argThat(token -> token instanceof UsernamePasswordAuthenticationToken
                && ((UsernamePasswordAuthenticationToken) token).getPrincipal().equals(signUpDto.getUsername())
                && ((UsernamePasswordAuthenticationToken) token).getCredentials().equals(signUpDto.getPassword())));

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Sign up successful", response.getMessage());
        Assertions.assertNotNull(response.getData());

    }

    @Test
    void createUser_Error_1() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("HA");
        signUpDto.setUsername("ha12");
        signUpDto.setEmail("email@gmail.com");
        signUpDto.setPassword("12345Aa@");
        signUpDto.setPhone("1234567890");
        // Mocking userRepository.existsByUsername() and userRepository.existsByEmail() to return false
        Mockito.when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);

        // Mocking tokenProvider.generateToken() to return a token
        Mockito.when(tokenProvider.generateToken(Mockito.any())).thenReturn("token");

        // Mocking userRepository.save() to return the user passed to it
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("abcdef");

        // Call the method under test
        OuSmartLockerResp response = authService.createUser(signUpDto);

        // Verify that userRepository.save() was called with the correct user
        Mockito.verify(userRepository).save(Mockito.argThat(user -> user.getUsername().equals(signUpDto.getUsername())));

        // Verify that tokenProvider.generateToken() was called with the correct parameters
        Mockito.verify(tokenProvider).generateToken(Mockito.argThat(token -> token instanceof UsernamePasswordAuthenticationToken
                && ((UsernamePasswordAuthenticationToken) token).getPrincipal().equals(signUpDto.getUsername())
                && ((UsernamePasswordAuthenticationToken) token).getCredentials().equals(signUpDto.getPassword())));

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals("Sign up successful", response.getMessage());
        Assertions.assertNotNull(response.getData());

    }
}