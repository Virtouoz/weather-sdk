package com.weather.sdk;

import com.weather.sdk.cache.WeatherCache;
import com.weather.sdk.client.WeatherClient;
import com.weather.sdk.exception.ApiKeyConflictException;
import com.weather.sdk.exception.SDKException;
import com.weather.sdk.mode.SDKMode;
import com.weather.sdk.model.WeatherResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Основной класс SDK для OpenWeatherMap.
 * <p>
 * Поддерживает:
 * - Уникальность по API-ключу
 * - Кэширование (10 городов, 10 минут)
 * - Режимы: on-demand и polling
 * </p>
 */
public final class WeatherSDK {

    private static final Map<String, WeatherSDK> INSTANCES = new ConcurrentHashMap<>();

    private final String apiKey;
    private final WeatherClient client;
    private final WeatherCache cache;
    private final SDKMode mode;

    private WeatherSDK(String apiKey, SDKMode mode) {
        this.apiKey = apiKey.trim();
        this.client = new WeatherClient(this.apiKey);
        this.cache = new WeatherCache();
        this.mode = mode;
        this.mode.start(this);
    }

    /**
     * Создаёт экземпляр SDK.
     * @throws ApiKeyConflictException если ключ уже используется
     */
    public static WeatherSDK create(String apiKey, SDKMode mode) throws ApiKeyConflictException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
        String key = apiKey.trim();
        if (INSTANCES.containsKey(key)) {
            throw new ApiKeyConflictException("SDK with API key '" + maskKey(key) + "' already exists");
        }
        WeatherSDK sdk = new WeatherSDK(key, mode);
        INSTANCES.put(key, sdk);
        return sdk;
    }

    /**
     * Возвращает погоду для города.
     * Использует кэш, если данные свежие.
     */
    public WeatherResponse getCurrentWeather(String cityName) throws SDKException {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty");
        }
        String city = cityName.trim();

        WeatherResponse cached = cache.get(city);
        if (cached != null) {
            return cached;
        }

        WeatherResponse fresh = client.fetchCurrentWeather(city);
        cache.put(city, fresh);
        return fresh;
    }

    /**
     * Принудительное обновление всех кэшированных городов.
     * Используется в polling-режиме.
     */
    public void refreshAll() {
        cache.getAllCities().forEach(city -> {
            try {
                WeatherResponse data = client.fetchCurrentWeather(city);
                cache.put(city, data);
            } catch (Exception e) {
                // Логи в будущем
            }
        });
    }

    /**
     * Освобождает ресурсы и удаляет экземпляр.
     */
    public void destroy() {
        mode.stop();
        INSTANCES.remove(apiKey);
    }

    private static String maskKey(String key) {
        if (key.length() <= 8) return "****";
        return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
    }

    static void clearInstances() {
        INSTANCES.values().forEach(WeatherSDK::destroy);
        INSTANCES.clear();
    }
}