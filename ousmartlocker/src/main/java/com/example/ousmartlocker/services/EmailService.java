package com.example.ousmartlocker.services;

import com.example.ousmartlocker.model.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}
