package com.authMS.Auth.microsservice.services;

import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.dtos.UserLoginDto;
import com.authMS.Auth.microsservice.exceptions.UserAlreadyRegistered;
import com.authMS.Auth.microsservice.exceptions.UserNotFound;
import com.authMS.Auth.microsservice.mappers.UserMapper;
import com.authMS.Auth.microsservice.models.UserModel;
import com.authMS.Auth.microsservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public void registerUser(UserDto userDto) {
        userRepository.findByEmail(userDto.email()).ifPresent((user) -> {
            throw new UserAlreadyRegistered("User already registered");
        });

        userRepository.save(userMapper.userDtoToModel(userDto));
    }

    public String getLoginToken(UserLoginDto userLoginDto) {
        UserModel userModel;

        if (userLoginDto.username().contains("@")) {
            userModel = userRepository.findByEmail(userLoginDto.username()).orElseThrow(() -> new UserNotFound("Invalid Credentials"));
        } else {
            userModel = userRepository.findByUsername(userLoginDto.username()).orElseThrow(() -> new UserNotFound("Invalid Credentials"));
        }

        if (!userModel.getPassword().equals(userLoginDto.password())) {
            throw new UserNotFound("Invalid Credentials");
        }

        return jwtService.generateToken(userMapper.userModelToDto(userModel));
    }

    public UserDto getUserByEmail(String email) {
        UserModel userModel = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound("Email not registered"));

        return userMapper.userModelToDto(userModel);
    }
}
