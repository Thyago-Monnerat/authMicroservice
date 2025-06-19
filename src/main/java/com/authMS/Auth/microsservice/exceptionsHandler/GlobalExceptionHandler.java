package com.authMS.Auth.microsservice.exceptionsHandler;

import com.authMS.Auth.microsservice.exceptions.JwtException;
import com.authMS.Auth.microsservice.exceptions.UserAlreadyRegistered;
import com.authMS.Auth.microsservice.exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handlerUserNotFound(UserNotFound e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handlerJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
