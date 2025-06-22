package com.authMS.Auth.microsservice.dtos;

public record LogMessageDto(String timestamp, String level, String message, String service) {
}
