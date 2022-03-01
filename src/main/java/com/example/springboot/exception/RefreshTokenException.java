package com.example.springboot.exception;

import java.lang.Exception;

public class RefreshTokenException extends Exception 
{ 
    public RefreshTokenException(String errorMessage) {
        super(errorMessage);
    }
}