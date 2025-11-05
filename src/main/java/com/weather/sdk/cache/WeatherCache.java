package com.weather.sdk.cache;

import com.weather.sdk.model.WeatherResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WeatherCache {
    private static final int MAX_SIZE = 10;
    private static final long TTL_MS = 10 * 60 * 1000; // 10 минут

    private final Map<String, CacheEntry> cache = new LinkedHashMap<>(MAX_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
            return size() > MAX_SIZE;
        }
    };

    public synchronized void put(String city, WeatherResponse weather) {
        cache.put(normalize(city), new CacheEntry(weather));
    }

    public synchronized WeatherResponse get(String city) {
        CacheEntry entry = cache.get(normalize(city));
        if (entry == null) return null;

        if (System.currentTimeMillis() - entry.timestamp > TTL_MS) {
            cache.remove(normalize(city));
            return null;
        }
        return entry.weather;
    }

    public synchronized Set<String> getAllCities() {
        return Set.copyOf(cache.keySet());
    }

    private String normalize(String city) {
        return city.toLowerCase().trim();
    }
}