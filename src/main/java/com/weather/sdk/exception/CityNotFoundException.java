package com.weather.sdk.exception;

public class CityNotFoundException extends SDKException {
    public CityNotFoundException(String message) {
        super(message);
    }
}