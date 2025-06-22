package com.authMS.Auth.microsservice.controllers;

import com.authMS.Auth.microsservice.dtos.LogMessageDto;
import com.authMS.Auth.microsservice.dtos.UserLoginDto;
import com.authMS.Auth.microsservice.services.KafkaProducerService;
import com.authMS.Auth.microsservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.authMS.Auth.microsservice.utils.CustomUtils.getStringTimeStamp;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("auth/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        String token = userService.getLoginToken(userLoginDto);
        kafkaProducerService.infoLogsSender(new LogMessageDto(getStringTimeStamp(), "INFO", "User " + userLoginDto.username() + " logged in successfully", "UserController"), "login");
        return ResponseEntity.ok().body(token);
    }
}
