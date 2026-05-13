# Chicago Weather App

A JavaFX desktop application that fetches and displays live weather data 
using the National Weather Service (NWS) API. Built for CS 342 
Software Design at UIC.

## Features
- 7-day weather forecast with daily conditions and temperatures
- Real-time precipitation chance and current conditions
- Sunrise and sunset times
- Wind data search
- Fahrenheit/Celsius temperature conversion

## Requirements
- Java 21 or higher
- Maven 3.6+

## Building and Running
```bash
mvn clean javafx:run
```

## Project Structure
- `src/main/java/` - Main source code
  - `JavaFX.java` - Main application entry point
  - `MyWeatherAPI.java` - Weather API integration
  - `weather/` - Weather data models (Period, Properties, Root, WeatherAPI)
- `src/test/java/` - Test code
- `pom.xml` - Maven configuration

## Dependencies
- JavaFX 19.0.2.1 - UI framework
- Jackson 2.11.3 - JSON parsing
- JUnit Jupiter 5.9.1 - Testing framework
