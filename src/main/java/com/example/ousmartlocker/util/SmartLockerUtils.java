package com.example.ousmartlocker.util;

import com.example.ousmartlocker.exception.OtpInvalidException;
import com.example.ousmartlocker.exception.SmsInvalidException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class SmartLockerUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return patternMatches(email, regexPattern);
    }

    private static boolean patternMatches(String s, String regexPattern) {
        return Pattern.compile(regexPattern).matcher(s).matches();
    }

    public static void validateExpireTime(String time){
        LocalDateTime expireTime = LocalDateTime.parse(time, SmartLockerUtils.formatter);
        LocalDateTime currentTime = LocalDateTime.now();
        if (expireTime.isBefore(currentTime))
            throw new OtpInvalidException("Expired time");
    }

    public static String formatPhoneNumber(String phone) {
        if (phone.length() == 10 && phone.matches("\\d+") && phone.startsWith("0")) {
            return "+84" + phone.substring(1);
        } else {
            throw new SmsInvalidException("Sms is invalid");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("+84") || phoneNumber.length() != 12 || !phoneNumber.substring(3).matches("\\d+")) {
            throw new SmsInvalidException("Sms is invalid");
        }
    }
}
