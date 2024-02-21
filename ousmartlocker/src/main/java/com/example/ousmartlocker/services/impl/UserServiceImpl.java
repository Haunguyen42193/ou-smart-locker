package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.entity.Role;
import com.example.ousmartlocker.entity.User;
import com.example.ousmartlocker.exception.PasswordInvalidException;
import com.example.ousmartlocker.exception.RoleAlreadyExistsException;
import com.example.ousmartlocker.exception.UserNotFoundException;
import com.example.ousmartlocker.model.ChangePassDto;
import com.example.ousmartlocker.model.ConvertData;
import com.example.ousmartlocker.model.OuSmartLockerResp;
import com.example.ousmartlocker.model.UserDto;
import com.example.ousmartlocker.repository.UserRepository;
import com.example.ousmartlocker.services.UserService;
import com.example.ousmartlocker.util.ReadToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadToken readToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OuSmartLockerResp getAllUser() {
        List<UserDto> userDto = userRepository.findAll().stream().map(ConvertData::convertUserToUserDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all user").data(userDto).build();
    }

    @Override
    public OuSmartLockerResp addRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (Objects.isNull(user))
            throw new UserNotFoundException("User not found");
        if (user.getRoles().contains(Role.ROLE_ADMIN))
            throw new RoleAlreadyExistsException("Add role fail. This user had role admin");
        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);
        UserDto userDto = ConvertData.convertUserToUserDto(user);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all user").data(userDto).build();
    }

    @Override
    public OuSmartLockerResp changePassword(ChangePassDto changePassDto) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            String uuid = readToken.getRequestAttribute(request, "userId");
            User user = userRepository.findById(Long.valueOf(uuid)).orElseThrow();
            if (passwordEncoder.matches(user.getPassword(), changePassDto.getOldPass()))
                throw new PasswordInvalidException("Password invalid!!");
            user.setPassword(passwordEncoder.encode(changePassDto.getNewPass()));
            userRepository.save(user);
            return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Change password success!").build();
        }
        return null;
    }
}
