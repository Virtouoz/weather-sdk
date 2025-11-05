package com.weather.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.sdk.exception.CityNotFoundException;
import com.weather.sdk.exception.NetworkException;
import com.weather.sdk.exception.SDKException;
import com.weather.sdk.model.WeatherResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherClient {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;

    public WeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public WeatherResponse fetchCurrentWeather(String city) throws SDKException {
        String url = String.format(BASE_URL, city, apiKey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            String body = response.body();

            if (status == 401) {
                throw new NetworkException("Invalid API key. Check 'appid' parameter and key.");
            }
            if (status == 404) {
                throw new CityNotFoundException("City '" + city + "' not found");
            }
            if (status == 429) {
                throw new NetworkException("Too many requests. Wait a minute.");
            }
            if (status >= 400) {
                throw new NetworkException("API error: " + status + " — " + body);
            }

            WeatherResponse weather = mapper.readValue(body, WeatherResponse.class);

            if (weather.getMain() == null || weather.getMain().getTemp() == null) {
                throw new SDKException("Invalid response: missing temperature data");
            }

            return weather;

        } catch (Exception e) {
            throw new SDKException("Failed to fetch or parse weather for '" + city + "': " + e.getMessage(), e);
        }
    }
}