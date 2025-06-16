package com.authMS.Auth.microsservice.mappers;

import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserModel userDtoToModel(UserDto userDto);
}
