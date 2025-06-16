package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.exceptions.UserAlreadyRegistered;
import com.authMS.Auth.microsservice.mappers.UserMapper;
import com.authMS.Auth.microsservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(UserDto userDto) {
        userRepository.findByEmail(userDto.email()).ifPresent((user) -> {
            throw new UserAlreadyRegistered("User already registered");
        });

        userRepository.save(userMapper.userDtoToModel(userDto));
    }
}
