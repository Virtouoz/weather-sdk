package com.weather.sdk.mode;

import com.weather.sdk.WeatherSDK;

public interface SDKMode {
    void start(WeatherSDK sdk);
    void stop();
    boolean isPolling();
}