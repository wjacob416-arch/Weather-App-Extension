package weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1


public class WeatherAPI {
    public static ArrayList<Period> getForecast(String region, int gridx, int gridy) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/gridpoints/"+region+"/"+String.valueOf(gridx)+","+String.valueOf(gridy)+"/forecast"))
                //.method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Root r = getObject(response.body());
        if(r == null){
            System.err.println("Failed to parse JSon");
            return null;
        }
        return r.properties.periods;
    }
    public static String[] getSunriseSunset(double lat, double lng) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng + "&formatted=0"))
                .header("User-Agent", "ChicagoWeatherApp/1.0")
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Sunrise API response: " + response.body());
            ObjectMapper om = new ObjectMapper();
            SunriseSunsetResult result = om.readValue(response.body(), SunriseSunsetResult.class);
            if (result == null || !"OK".equals(result.status)) return null;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");
            String sunrise = OffsetDateTime.parse(result.results.sunrise)
                    .atZoneSameInstant(ZoneId.systemDefault()).format(fmt);
            String sunset = OffsetDateTime.parse(result.results.sunset)
                    .atZoneSameInstant(ZoneId.systemDefault()).format(fmt);
            return new String[]{sunrise, sunset};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Root getObject(String json){
        ObjectMapper om = new ObjectMapper();
        Root toRet = null;
        try {
            toRet = om.readValue(json, Root.class);
            ArrayList<Period> p = toRet.properties.periods;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return toRet;

    }
}



