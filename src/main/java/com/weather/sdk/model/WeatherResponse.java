package com.weather.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("weather")
    private Weather[] weather;
    @JsonProperty("main")
    private Main main;
    private Integer visibility;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("dt")
    private Long dt;
    @JsonProperty("sys")
    private Sys sys;
    private Integer timezone;
    private String name;

    public Weather getWeather() { return weather != null && weather.length > 0 ? weather[0] : null; }
    public Main getMain() { return main; }
    public Integer getVisibility() { return visibility; }
    public Wind getWind() { return wind; }
    public Long getDt() { return dt; }
    public Sys getSys() { return sys; }
    public Integer getTimezone() { return timezone; }
    public String getName() { return name; }
}