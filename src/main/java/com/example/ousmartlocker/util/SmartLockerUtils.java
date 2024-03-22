package com.example.ousmartlocker.util;

import com.example.ousmartlocker.exception.OtpInvalidException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class SmartLockerUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
}
