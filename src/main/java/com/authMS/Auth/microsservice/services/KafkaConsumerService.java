package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.LogMessageDto;
import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.exceptions.UserAlreadyRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.authMS.Auth.microsservice.utils.CustomUtils.getStringTimeStamp;

@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(
            topics = "user-register",
            groupId = "user-handler",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenUserRegister(UserDto userDto) {
        try {
            userService.registerUser(userDto);
            kafkaProducerService.infoLogsSender(new LogMessageDto(getStringTimeStamp(), "INFO", "User " + userDto.username() + " registered successfully", "KafkaConsumerService"), "register");
        } catch (UserAlreadyRegistered e) {
            kafkaProducerService.errorLogsSender(new LogMessageDto(getStringTimeStamp(), "ERROR", e.getMessage(), "KafkaConsumer"), e.getClass().getSimpleName().toLowerCase());
        }
    }
}
