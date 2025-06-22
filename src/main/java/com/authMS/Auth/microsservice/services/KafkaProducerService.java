package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, LogMessage> authLogTemplate;

    public void infoLogsSender(LogMessage LogMessage, String key) {
        authLogTemplate.send("info-logs", key, LogMessage);
    }

    public void errorLogsSender(LogMessage LogMessage, String key) {
        authLogTemplate.send("error-logs", key, LogMessage);
    }

}
