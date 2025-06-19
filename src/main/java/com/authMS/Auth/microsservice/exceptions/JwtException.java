package com.authMS.Auth.microsservice.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String message){
        super(message);
    }
}
