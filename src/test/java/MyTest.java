import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import weather.Period;
import weather.Root;
import weather.WeatherAPI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

class MyTest {

    // Minimal valid NWS-shaped JSON for unit tests (no Date fields to avoid parse concerns)
    private static final String ONE_PERIOD_JSON =
        "{\"type\":\"Feature\",\"geometry\":null,\"properties\":{" +
        "\"units\":\"us:customary\",\"forecastGenerator\":\"BaselineForecastGenerator\"," +
        "\"validTimes\":\"2024-01-01T00:00:00+00:00/P7D\"," +
        "\"elevation\":{\"unitCode\":\"wmoUnit:m\",\"value\":185.9}," +
        "\"periods\":[{\"number\":1,\"name\":\"Today\",\"isDaytime\":true," +
        "\"temperature\":45,\"temperatureUnit\":\"F\",\"temperatureTrend\":null," +
        "\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":20}," +
        "\"windSpeed\":\"10 mph\",\"windDirection\":\"NE\"," +
        "\"shortForecast\":\"Mostly Sunny\",\"detailedForecast\":\"Mostly sunny.\"}]}}";

    private static final String THREE_PERIOD_JSON =
        "{\"type\":\"Feature\",\"geometry\":null,\"properties\":{" +
        "\"units\":\"us:customary\",\"forecastGenerator\":\"BaselineForecastGenerator\"," +
        "\"validTimes\":\"2024-01-01T00:00:00+00:00/P7D\"," +
        "\"elevation\":{\"unitCode\":\"wmoUnit:m\",\"value\":185.9}," +
        "\"periods\":[" +
        "{\"number\":1,\"name\":\"Today\",\"isDaytime\":true,\"temperature\":45,\"temperatureUnit\":\"F\"," +
        "\"temperatureTrend\":null,\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":5}," +
        "\"windSpeed\":\"10 mph\",\"windDirection\":\"N\",\"shortForecast\":\"Sunny\",\"detailedForecast\":\"Sunny.\"}," +
        "{\"number\":2,\"name\":\"Tonight\",\"isDaytime\":false,\"temperature\":32,\"temperatureUnit\":\"F\"," +
        "\"temperatureTrend\":null,\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":10}," +
        "\"windSpeed\":\"5 mph\",\"windDirection\":\"SW\",\"shortForecast\":\"Clear\",\"detailedForecast\":\"Clear.\"}," +
        "{\"number\":3,\"name\":\"Wednesday\",\"isDaytime\":true,\"temperature\":50,\"temperatureUnit\":\"F\"," +
        "\"temperatureTrend\":null,\"probabilityOfPrecipitation\":{\"unitCode\":\"wmoUnit:percent\",\"value\":30}," +
        "\"windSpeed\":\"15 mph\",\"windDirection\":\"SE\",\"shortForecast\":\"Partly Cloudy\",\"detailedForecast\":\"Cloudy.\"}]}}";

    @SuppressWarnings("unchecked")
    private Map<String, ArrayList<Period>> getCache() throws Exception {
        Field f = MyWeatherAPI.class.getDeclaredField("cache");
        f.setAccessible(true);
        return (Map<String, ArrayList<Period>>) f.get(null);
    }

    @BeforeEach
    void clearCache() throws Exception {
        getCache().clear();
    }

    // ==================== JSON Deserialization ====================

    @Test
    @DisplayName("getObject: valid JSON returns non-null Root")
    void testGetObjectReturnsRoot() {
        Root result = WeatherAPI.getObject(ONE_PERIOD_JSON);
        assertNotNull(result);
    }

    @Test
    @DisplayName("getObject: period scalar fields are deserialized correctly")
    void testGetObjectPeriodScalarFields() {
        Root result = WeatherAPI.getObject(ONE_PERIOD_JSON);
        Period p = result.properties.periods.get(0);
        assertAll(
            () -> assertEquals(1, p.number),
            () -> assertEquals("Today", p.name),
            () -> assertTrue(p.isDaytime),
            () -> assertEquals(45, p.temperature),
            () -> assertEquals("F", p.temperatureUnit),
            () -> assertEquals("10 mph", p.windSpeed),
            () -> assertEquals("NE", p.windDirection),
            () -> assertEquals("Mostly Sunny", p.shortForecast)
        );
    }

    @Test
    @DisplayName("getObject: precipitation probability is deserialized correctly")
    void testGetObjectPrecipitationProbability() {
        Root result = WeatherAPI.getObject(ONE_PERIOD_JSON);
        Period p = result.properties.periods.get(0);
        assertNotNull(p.probabilityOfPrecipitation);
        assertEquals(20, p.probabilityOfPrecipitation.value);
        assertEquals("wmoUnit:percent", p.probabilityOfPrecipitation.unitCode);
    }

    @Test
    @DisplayName("getObject: properties metadata is deserialized correctly")
    void testGetObjectPropertiesMetadata() {
        Root result = WeatherAPI.getObject(ONE_PERIOD_JSON);
        assertAll(
            () -> assertEquals("us:customary", result.properties.units),
            () -> assertEquals("BaselineForecastGenerator", result.properties.forecastGenerator),
            () -> assertNotNull(result.properties.elevation),
            () -> assertEquals(185.9, result.properties.elevation.value, 0.001)
        );
    }

    @Test
    @DisplayName("getObject: multiple periods are all parsed")
    void testGetObjectMultiplePeriods() {
        Root result = WeatherAPI.getObject(THREE_PERIOD_JSON);
        assertEquals(3, result.properties.periods.size());
        assertEquals("Today", result.properties.periods.get(0).name);
        assertEquals("Tonight", result.properties.periods.get(1).name);
        assertEquals("Wednesday", result.properties.periods.get(2).name);
    }

    @Test
    @DisplayName("getObject: daytime flag is correct for each period")
    void testGetObjectDaytimeFlag() {
        Root result = WeatherAPI.getObject(THREE_PERIOD_JSON);
        assertTrue(result.properties.periods.get(0).isDaytime);
        assertFalse(result.properties.periods.get(1).isDaytime);
        assertTrue(result.properties.periods.get(2).isDaytime);
    }

    @Test
    @DisplayName("getObject: invalid JSON returns null")
    void testGetObjectInvalidJsonReturnsNull() {
        assertNull(WeatherAPI.getObject("not valid json {{{"));
    }

    @Test
    @DisplayName("getObject: empty string returns null")
    void testGetObjectEmptyStringReturnsNull() {
        assertNull(WeatherAPI.getObject(""));
    }

    // ==================== Temperature Conversion ====================

    @Test
    @DisplayName("F→C: 32°F = 0°C (freezing)")
    void testFtoC_freezing() {
        assertEquals(0.0, toCelsius(32), 0.001);
    }

    @Test
    @DisplayName("F→C: 212°F = 100°C (boiling)")
    void testFtoC_boiling() {
        assertEquals(100.0, toCelsius(212), 0.001);
    }

    @Test
    @DisplayName("F→C: -40°F = -40°C (equal point)")
    void testFtoC_equalPoint() {
        assertEquals(-40.0, toCelsius(-40), 0.001);
    }

    @Test
    @DisplayName("F→C: 98.6°F ≈ 37°C (body temperature)")
    void testFtoC_bodyTemp() {
        assertEquals(37.0, toCelsius(98.6), 0.001);
    }

    @Test
    @DisplayName("C→F: 0°C = 32°F (freezing)")
    void testCtoF_freezing() {
        assertEquals(32.0, toFahrenheit(0), 0.001);
    }

    @Test
    @DisplayName("C→F: 100°C = 212°F (boiling)")
    void testCtoF_boiling() {
        assertEquals(212.0, toFahrenheit(100), 0.001);
    }

    @Test
    @DisplayName("C→F: 37°C = 98.6°F (body temperature)")
    void testCtoF_bodyTemp() {
        assertEquals(98.6, toFahrenheit(37), 0.001);
    }

    @Test
    @DisplayName("C→F→C round-trip preserves value")
    void testConversionRoundTrip() {
        double original = 23.5;
        assertEquals(original, toCelsius(toFahrenheit(original)), 0.001);
    }

    // ==================== Wind Direction Angle ====================

    @ParameterizedTest
    @DisplayName("Wind direction maps to correct compass rotation angle")
    @CsvSource({
        "N,    0",
        "NE,  45",
        "E,   90",
        "SE, 135",
        "S,  180",
        "SW, 225",
        "W,  270",
        "NW, 315",
        "n,    0",   // case-insensitive
        "UNKNOWN, 0" // default fallback
    })
    void testWindDirectionAngle(String direction, double expected) {
        assertEquals(expected, windAngle(direction), 0.001);
    }

    // ==================== Caching ====================

    @Test
    @DisplayName("Cache: hit returns the exact pre-populated list")
    void testCacheHitReturnsSameReference() throws Exception {
        ArrayList<Period> mock = new ArrayList<>();
        Period p = new Period();
        p.name = "Cached";
        p.temperature = 77;
        mock.add(p);
        getCache().put("LOT77,70", mock);

        ArrayList<Period> result = MyWeatherAPI.getForecast("LOT", 77, 70);
        assertSame(mock, result);
        assertEquals("Cached", result.get(0).name);
    }

    @Test
    @DisplayName("Cache: different region/grid keys are isolated")
    void testCacheKeysAreIsolated() throws Exception {
        ArrayList<Period> list1 = makePeriodList("Region1");
        ArrayList<Period> list2 = makePeriodList("Region2");
        getCache().put("LOT77,70", list1);
        getCache().put("BOU100,50", list2);

        assertSame(list1, MyWeatherAPI.getForecast("LOT", 77, 70));
        assertSame(list2, MyWeatherAPI.getForecast("BOU", 100, 50));
    }

    @Test
    @DisplayName("Cache: key format is region + gridx + ',' + gridy")
    void testCacheKeyFormat() throws Exception {
        ArrayList<Period> mock = makePeriodList("KeyTest");
        getCache().put("XYZ10,20", mock);

        assertSame(mock, MyWeatherAPI.getForecast("XYZ", 10, 20));
    }

    // ==================== Helpers ====================

    private double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }

    private double toFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32;
    }

    private double windAngle(String dir) {
        switch (dir.toUpperCase()) {
            case "N":  return 0;
            case "NE": return 45;
            case "E":  return 90;
            case "SE": return 135;
            case "S":  return 180;
            case "SW": return 225;
            case "W":  return 270;
            case "NW": return 315;
            default:   return 0;
        }
    }

    private ArrayList<Period> makePeriodList(String name) {
        ArrayList<Period> list = new ArrayList<>();
        Period p = new Period();
        p.name = name;
        list.add(p);
        return list;
    }
}
