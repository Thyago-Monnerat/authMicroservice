package com.authMS.Auth.microsservice.exceptionsHandler;

import com.authMS.Auth.microsservice.dtos.LogMessageDto;
import com.authMS.Auth.microsservice.exceptions.JwtException;
import com.authMS.Auth.microsservice.exceptions.UserNotFound;
import com.authMS.Auth.microsservice.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.authMS.Auth.microsservice.utils.CustomUtils.getStringTimeStamp;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final KafkaProducerService kafkaProducerService;

    private void sendLogs(String msg, String service, String key) {
        kafkaProducerService.errorLogsSender(new LogMessageDto(getStringTimeStamp(), "ERROR", msg, service), key);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handlerUserNotFound(UserNotFound e) {
        sendLogs(e.getMessage(), e.getClass().toString(), e.getClass().getSimpleName().toLowerCase());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handlerJwtException(JwtException e) {
        sendLogs(e.getMessage(), e.getClass().toString(), e.getClass().getSimpleName().toLowerCase());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
