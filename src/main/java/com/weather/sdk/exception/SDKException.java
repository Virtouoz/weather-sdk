package com.weather.sdk.exception;

public class SDKException extends Exception {
    public SDKException(String message) {
        super(message);
    }
    public SDKException(String message, Throwable cause) {
        super(message, cause);
    }
}