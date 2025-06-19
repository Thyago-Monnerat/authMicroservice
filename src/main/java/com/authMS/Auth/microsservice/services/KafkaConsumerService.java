package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.exceptions.UserAlreadyRegistered;
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
       try{
           userService.registerUser(userDto);
       }catch (UserAlreadyRegistered e){
           System.out.println(e.getMessage());
       }
    }
}
