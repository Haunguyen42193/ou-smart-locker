package com.example.ousmartlocker.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String usernameOrEmail);
}
