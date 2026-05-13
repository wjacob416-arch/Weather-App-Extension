package weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SunriseSunsetResult {
    public Results results;
    public String status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results {
        public String sunrise;
        public String sunset;
    }
}
