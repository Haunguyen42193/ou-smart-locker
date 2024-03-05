package com.example.ousmartlocker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class SmartLockerUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime currentTime = LocalDateTime.now();

    public static boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return patternMatches(email, regexPattern);
    }

    private static boolean patternMatches(String s, String regexPattern) {
        return Pattern.compile(regexPattern).matcher(s).matches();
    }
}