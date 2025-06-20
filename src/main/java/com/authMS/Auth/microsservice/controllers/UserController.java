package com.authMS.Auth.microsservice.controllers;

import com.authMS.Auth.microsservice.dtos.UserLoginDto;
import com.authMS.Auth.microsservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("auth/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok().body(userService.getLoginToken(userLoginDto));
    }

}
