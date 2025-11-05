package com.weather.sdk.cache;

import com.weather.sdk.model.WeatherResponse;

class CacheEntry {
    final WeatherResponse weather;
    final long timestamp;

    CacheEntry(WeatherResponse weather) {
        this.weather = weather;
        this.timestamp = System.currentTimeMillis();
    }
}