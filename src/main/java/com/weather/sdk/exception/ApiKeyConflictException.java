package com.weather.sdk.exception;

public class ApiKeyConflictException extends SDKException {
    public ApiKeyConflictException(String message) {
        super(message);
    }
}