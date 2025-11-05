package com.weather.sdk.mode;

import com.weather.sdk.WeatherSDK;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PollingMode implements SDKMode {
    private ScheduledExecutorService executor;

    @Override
    public void start(WeatherSDK sdk) {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(sdk::refreshAll, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Override
    public boolean isPolling() {
        return true;
    }
}