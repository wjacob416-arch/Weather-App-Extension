# Chicago Weather App

A JavaFX desktop application that displays live weather data for Chicago using the National Weather Service (NWS) API and the Sunrise-Sunset API. Built for CS 342 Software Design at UIC.

## Features

- **7-day forecast** — daily conditions and high temperatures pulled from the NWS grid API
- **Current conditions** — today's short forecast and temperature shown in the header
- **Precipitation chance** — today's probability of precipitation
- **Sunrise & sunset times** — real astronomical times from the Sunrise-Sunset API, converted to local time
- **Wind data** — wind speed and direction with a rotating compass graphic
- **Temperature converter** — convert between Fahrenheit and Celsius

## Requirements

- Java 21+
- Maven 3.6+
- Internet connection (live API calls on startup)

## Building and Running

```bash
mvn clean javafx:run
```

## Project Structure

```
src/main/java/
├── JavaFX.java              # Main application entry point and UI
├── MyWeatherAPI.java        # (legacy) weather API helper
└── weather/
    ├── WeatherAPI.java      # NWS forecast + Sunrise-Sunset API calls
    ├── Period.java          # Forecast period data model
    ├── SunriseSunsetResult.java  # Sunrise-Sunset API response model
    ├── Root.java            # NWS API response root
    ├── Properties.java      # NWS API properties wrapper
    ├── ProbabilityOfPrecipitation.java
    ├── Elevation.java
    └── Geometry.java
src/main/resources/
└── styles.css               # Application stylesheet
```

## APIs Used

| API | Purpose |
|-----|---------|
| [api.weather.gov](https://www.weather.gov/documentation/services-web-api) | 7-day forecast, wind, precipitation |
| [api.sunrise-sunset.org](https://sunrise-sunset.org/api) | Sunrise and sunset times |

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| JavaFX Controls | 19.0.2.1 | UI framework |
| JavaFX FXML | 19.0.2.1 | FXML support |
| Jackson Databind | 2.11.3 | JSON parsing |
| JUnit Jupiter | 5.9.1 | Testing |
