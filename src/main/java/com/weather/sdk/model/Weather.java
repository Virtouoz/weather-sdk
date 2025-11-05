package com.weather.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private String main;
    private String description;

    public String getMain() { return main; }
    public String getDescription() { return description; }
}