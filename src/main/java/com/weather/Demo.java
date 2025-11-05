package com.weather;

import com.weather.sdk.WeatherSDK;
import com.weather.sdk.exception.ApiKeyConflictException;
import com.weather.sdk.exception.SDKException;
import com.weather.sdk.mode.OnDemandMode;
import com.weather.sdk.model.WeatherResponse;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = "b000df72060975b8875df033476912ea";

        try {
            WeatherSDK sdk = WeatherSDK.create(apiKey, new OnDemandMode());

            long startTime1 = System.nanoTime();
            // Первый запрос — с API
            System.out.println("=== Запрос 1 (с API) ===");
            WeatherResponse w1 = sdk.getCurrentWeather("Moscow");
            printWeather(w1);
            long endTime1 = System.nanoTime();
            Thread.sleep(1000);

            // Второй запрос — из кэша
            long startTime2 = System.nanoTime();
            System.out.println("\n=== Запрос 2 (из кэша) ===");
            WeatherResponse w2 = sdk.getCurrentWeather("Moscow");
            printWeather(w2);
            long endTime2 = System.nanoTime();

            System.out.println(endTime1 - startTime1);
            System.out.println(endTime2 - startTime2);

            // Очистка
            sdk.destroy();

        } catch (ApiKeyConflictException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (SDKException e) {
            System.out.println("Ошибка API: " + e.getMessage());
        }
    }

    private static void printWeather(WeatherResponse w) {
        System.out.println("Город: " + w.getName());
        System.out.println("Температура: " + w.getMain().getTemp() + "°C");
        System.out.println("Ощущается: " + w.getMain().getFeelsLike() + "°C");
        System.out.println("Погода: " + w.getWeather().getDescription());
        System.out.println("Ветер: " + w.getWind().getSpeed() + " м/с");
    }
}