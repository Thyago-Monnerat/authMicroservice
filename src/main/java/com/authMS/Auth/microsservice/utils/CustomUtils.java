package com.authMS.Auth.microsservice.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomUtils {

    public static String getStringTimeStamp(){
        return LocalDateTime.now().toString();
    }
}
