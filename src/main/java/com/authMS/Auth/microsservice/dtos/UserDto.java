package com.authMS.Auth.microsservice.dtos;

import com.authMS.Auth.microsservice.enums.Role;

public record UserDto(String username, String password, String email, Role role) {
}
