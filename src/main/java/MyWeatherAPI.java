import weather.WeatherAPI;
import weather.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class MyWeatherAPI extends WeatherAPI {
    // Cache to store forecasts for specific region and grid coordinates
    private static final Map<String, ArrayList<Period>> cache = new HashMap<>();

    public static ArrayList<Period> getForecast(String region, int gridx, int gridy) {
        String cacheKey = region + gridx + "," + gridy; // Create a unique cache key

        // Check if the forecast is already cached
        if (cache.containsKey(cacheKey)) {
            System.out.println("Returning cached forecast for: " + cacheKey);
            return cache.get(cacheKey);
        }
        ArrayList<Period> forecast = WeatherAPI.getForecast(region, gridx, gridy);
        // Validate and store the result in the cache
        if (forecast != null) {
            cache.put(cacheKey, forecast);
        } else {
            System.err.println("WeatherAPI returned null for: " + cacheKey);
        }

        return forecast;
    }






}
