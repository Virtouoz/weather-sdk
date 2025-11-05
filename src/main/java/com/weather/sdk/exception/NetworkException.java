package com.weather.sdk.exception;

public class NetworkException extends SDKException {
    public NetworkException(String message) {
        super(message);
    }
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}