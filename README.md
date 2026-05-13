# Chicago Weather App

A JavaFX desktop application that fetches and displays live weather data for Chicago. Built for CS 342 Software Design at UIC.

---

## Features

| Feature | Details |
|---------|---------|
| 7-Day Forecast | Daily conditions and temperatures from the NWS grid API |
| Current Conditions | Today's temperature and short forecast shown in the header |
| Precipitation Chance | Today's probability of precipitation |
| Sunrise & Sunset | Real astronomical times, converted to local timezone |
| Wind Data | Speed and direction with a rotating compass graphic |
| Temperature Converter | Interactive F ↔ C converter in a pop-up window |
| Forecast Caching | Responses are cached so repeated lookups skip the network |

---

## Requirements

- Java 21+
- Maven 3.6+
- Internet connection (live API calls on startup)

---

## Running the App

```bash
mvn clean javafx:run
```

---

## Running the Tests

```bash
mvn test
```

The test suite covers:
- **JSON deserialization** — `WeatherAPI.getObject()` parses NWS API responses correctly
- **Temperature conversion** — F→C and C→F formulas (freezing, boiling, body temp, round-trip)
- **Wind direction mapping** — all 8 compass directions map to the correct rotation angle
- **Caching** — `MyWeatherAPI` returns cached data and isolates keys by region/grid

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── JavaFX.java              # Application entry point and all UI logic
│   │   ├── MyWeatherAPI.java        # Caching wrapper around WeatherAPI
│   │   └── weather/
│   │       ├── WeatherAPI.java      # HTTP calls to NWS and Sunrise-Sunset APIs
│   │       ├── Period.java          # Single forecast period (temp, wind, etc.)
│   │       ├── Root.java            # NWS API root response model
│   │       ├── Properties.java      # NWS properties wrapper (holds periods list)
│   │       ├── SunriseSunsetResult.java
│   │       ├── ProbabilityOfPrecipitation.java
│   │       ├── Elevation.java
│   │       └── Geometry.java
│   └── resources/
│       └── styles.css               # Dark-theme stylesheet
└── test/
    └── java/
        └── MyTest.java              # JUnit 5 test suite (29 tests)
```

---

## APIs Used

| API | Endpoint | Used For |
|-----|----------|----------|
| National Weather Service | `api.weather.gov/gridpoints/LOT/77,70/forecast` | 7-day forecast, wind, precipitation |
| Sunrise-Sunset | `api.sunrise-sunset.org/json` | Sunrise and sunset times for Chicago |

---

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| JavaFX Controls | 19.0.2.1 | UI framework |
| JavaFX FXML | 19.0.2.1 | FXML support |
| Jackson Databind | 2.11.3 | JSON deserialization |
| JUnit Jupiter | 5.9.1 | Unit testing |
