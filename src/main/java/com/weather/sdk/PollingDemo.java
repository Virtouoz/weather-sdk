package com.weather.sdk;

import com.weather.sdk.WeatherSDK;
import com.weather.sdk.exception.ApiKeyConflictException;
import com.weather.sdk.exception.SDKException;
import com.weather.sdk.mode.PollingMode;
import com.weather.sdk.model.WeatherResponse;

public class PollingDemo {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = "b000df72060975b8875df033476912ea";

        WeatherSDK sdk = null;
        try {
            // 1. Создаём SDK в Polling-режиме
            sdk = WeatherSDK.create(apiKey, new PollingMode());
            System.out.println("Polling-режим запущен (обновление каждые 5 минут)");

            // 2. Первый запрос — с API
            System.out.println("\n=== Запрос 1 (с API) ===");
            long start1 = System.nanoTime();
            WeatherResponse w1 = sdk.getCurrentWeather("Moscow");
            long time1 = System.nanoTime() - start1;
            printWeather(w1, time1);

            // 3. Ждём 10 секунд (для демо)
            System.out.println("\nЖдём 10 секунд... (в реальности — 5 минут)");
            Thread.sleep(10000);

            // 4. Второй запрос — из кэша (быстро)
            System.out.println("\n=== Запрос 2 (из кэша) ===");
            long start2 = System.nanoTime();
            WeatherResponse w2 = sdk.getCurrentWeather("Moscow");
            long time2 = System.nanoTime() - start2;
            printWeather(w2, time2);

            // 5. Добавим ещё город
            sdk.getCurrentWeather("London");

            System.out.println("\nДобавлено 2 города в кэш: Moscow, London");
            System.out.println("Через 5 минут — оба обновятся автоматически!");

            // В реальном проекте — оставь работать
            // Thread.sleep(5 * 60 * 1000); // 5 минут

        } catch (ApiKeyConflictException e) {
            System.out.println("Ключ уже используется: " + e.getMessage());
        } catch (SDKException e) {
            System.out.println("Ошибка API: " + e.getMessage());
        } finally {
            if (sdk != null) {
                sdk.destroy();
                System.out.println("\nSDK уничтожен");
            }
        }
    }

    private static void printWeather(WeatherResponse w, long timeNs) {
        System.out.println("Город: " + w.getName());
        System.out.println("Температура: " + w.getMain().getTemp() + "°C");
        System.out.println("Ощущается: " + w.getMain().getFeelsLike() + "°C");
        System.out.println("Погода: " + w.getWeather().getDescription());
        System.out.println("Время запроса: " + (timeNs / 1_000_000.0) + " мс");
    }
}