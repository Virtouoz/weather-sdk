package com.weather.sdk.mode;

import com.weather.sdk.WeatherSDK;

public class OnDemandMode implements SDKMode {
    @Override
    public void start(WeatherSDK sdk) {
        // Ничего не делаем
    }

    @Override
    public void stop() {
        // Ничего не делаем
    }

    @Override
    public boolean isPolling() {
        return false;
    }
}