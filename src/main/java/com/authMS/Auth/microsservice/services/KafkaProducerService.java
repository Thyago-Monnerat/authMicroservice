package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.LogMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, LogMessageDto> authLogTemplate;

    public void infoLogsSender(LogMessageDto LogMessageDto, String key) {
        authLogTemplate.send("info-logs", key, LogMessageDto);
    }

    public void errorLogsSender(LogMessageDto LogMessageDto, String key) {
        authLogTemplate.send("error-logs", key, LogMessageDto);
    }

}
