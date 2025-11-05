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
```

---

## Installation

### 1. Clone & Build
```bash
git clone https://github.com/yourusername/weather-sdk.git
cd weather-sdk
mvn clean package
```
Output: `target/weather-sdk-1.0.0.jar`

### 2. Add as Dependency (Maven)

```
<dependency>
    <groupId>com.weather</groupId>
    <artifactId>weather-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

```bash
mvn install:install-file \
  -Dfile=target/weather-sdk-1.0.0.jar \
  -DgroupId=com.weather \
  -DartifactId=weather-sdk \
  -Dversion=1.0.0 \
  -Dpackaging=jar
```
---
## API

### `WeatherSDK.create(String apiKey, SDKMode mode)`
- Creates or returns existing SDK instance
- Throws `ApiKeyConflictException` if key is already in use

### `WeatherResponse getCurrentWeather(String city)`
- Returns cached data if valid
- Otherwise fetches from OpenWeatherMap
- Throws `CityNotFoundException`, `NetworkException`

### `void refreshAll()`
- Forces refresh of all cached cities (used in Polling mode)

### `void destroy()`
- Stops background tasks
- Removes instance from registry
- Releases resources

---
## Modes

| Mode          | Description |
|---------------|-------------|
| `OnDemandMode`| Data fetched only on `getCurrentWeather()` |
| `PollingMode` | Background thread refreshes all cached cities every 5 minutes |

```java
WeatherSDK.create(apiKey, new PollingMode());
```
## Caching

- **Max size**: 10 cities
- **TTL**: 10 minutes
- **Eviction**: LRU (Least Recently Used)
- **Thread-safe**: `synchronized` access

---
## Error Handling

```java
try {
    WeatherResponse w = sdk.getCurrentWeather("InvalidCity");
} catch (CityNotFoundException e) {
    System.out.println("City not found: " + e.getMessage());
} catch (NetworkException e) {
    System.out.println("Network error: " + e.getMessage());
}
```
---
## Demo Applications

- `Demo.java` — on-demand usage
- `PollingDemo.java` — auto-refresh demo

Run:
```bash
java -cp target/weather-sdk-1.0.0.jar com.weather.sdk.Demo
```
---
## Project Structure
```
src/
├── main/java/com/weather/sdk/
│   ├── WeatherSDK.java
│   ├── client/WeatherClient.java
│   ├── cache/WeatherCache.java
│   ├── mode/SDKMode.java, OnDemandMode.java, PollingMode.java
│   ├── model/*java (POJOs with @JsonIgnoreProperties)
│   ├── exception/*java
│   └── Demo.java, PollingDemo.java
└── test/java/com/weather/sdk/WeatherSDKTest.java
```
---
## License

MIT

---

**Built with Java 25 • OpenWeatherMap API • Maven**