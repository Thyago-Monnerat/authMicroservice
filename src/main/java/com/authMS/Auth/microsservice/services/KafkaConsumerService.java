package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    private final UserService userService;

    @KafkaListener(
            topics = "user-events",
            groupId = "user-register",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenUserRegister(UserDto userDto) {
        userService.registerUser(userDto);
    }
}
