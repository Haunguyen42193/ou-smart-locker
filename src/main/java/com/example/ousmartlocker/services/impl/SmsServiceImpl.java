package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Value("AC185041a00d1d9996a39ca8b6ea96156d")
    private String ACCOUNT_SID;
    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    public void sendSms(String phone, String content) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phone),
                        new com.twilio.type.PhoneNumber("+12515722135"),
                        content)
                .create();
    }
}
