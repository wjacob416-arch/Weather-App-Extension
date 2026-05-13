# Project2

A JavaFX weather application that fetches and displays weather data using the National Weather Service API.

## Requirements

- Java 21 or higher
- Maven 3.6+

## Building and Running

To build and run the application:

```bash
mvn clean javafx:run
```

The application will compile, download dependencies, and launch the JavaFX window.

## Project Structure

- `src/main/java/` - Main source code
  - `JavaFX.java` - Main application entry point
  - `MyWeatherAPI.java` - Weather API integration
  - `weather/` - Weather data models (Period, Properties, Root, WeatherAPI, etc.)
- `src/test/java/` - Test code
- `pom.xml` - Maven configuration

## Dependencies

- JavaFX 19.0.2.1 - UI framework
- Jackson 2.11.3 - JSON parsing
- JUnit Jupiter 5.9.1 - Testing framework
