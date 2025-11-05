# Weather SDK

**A lightweight Java SDK for OpenWeatherMap with caching, operational modes, and API key uniqueness.**

---

## Features

- **Caching**: Up to 10 cities, 10-minute TTL, LRU eviction policy
- **Operational Modes**:
    - `OnDemandMode` — fetch weather only when requested
    - `PollingMode` — background auto-refresh every 5 minutes
- **API Key Uniqueness**: Only one SDK instance per API key
- **Lifecycle Management**: `destroy()` to release resources
- **Exception Handling**:
    - `ApiKeyConflictException`
    - `CityNotFoundException`
    - `NetworkException`
    - `SDKException`
- **Tech Stack**: Java 25, Maven, Jackson, SLF4J + Logback

---

## Quick Start

```java
import com.weather.sdk.WeatherSDK;
import com.weather.sdk.mode.OnDemandMode;
import com.weather.sdk.model.WeatherResponse;

public class Example {
    public static void main(String[] args) {
        String apiKey = "your-api-key-here";

        try {
            WeatherSDK sdk = WeatherSDK.create(apiKey, new OnDemandMode());

            WeatherResponse weather = sdk.getCurrentWeather("Moscow");
            System.out.printf("Temperature in %s: %.1f°C%n",
                    weather.getName(),
                    weather.getMain().getTemp());

            sdk.destroy();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}