package com.example.demo.utils;

import io.micrometer.common.util.StringUtils;

public class Validation {
    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String emailRegex = "^(.+)@(\\S+)$";
        return email.matches(emailRegex);
    }
}
