package at.vaaniicx.endpoint;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import at.vaaniicx.dao.response.activity.GeoApifyResponse;
import at.vaaniicx.dao.response.weather.ForecastResponse;
import at.vaaniicx.dao.response.weather.Weather;
import at.vaaniicx.mapper.WeatherResponseMapper;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/result")
public class RestController {
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";

    private static final String GEOAPIFY_API_URL = "https://api.geoapify.com/v2/places";

    @GetMapping
    public ResponseEntity<GeoApifyResponse> getActivityByLocationAndWeather(
            @RequestParam(name = "latitude") float latitude,
            @RequestParam(name = "longitude") float longitude, @RequestParam(name = "radius") int radius,
            @RequestParam(name = "start_date") LocalDate startDate,
            @RequestParam(name = "end_date") LocalDate endDate) {

        Weather weatherResponse = WeatherResponseMapper.map(Objects.requireNonNull(
                new RestTemplate().getForEntity(buildWeatherUrlTemplate(), ForecastResponse.class,
                        getWeatherQueryParams(latitude, longitude, startDate, endDate)).getBody()));

        return new RestTemplate().getForEntity(buildPlacesUrlTemplate(), GeoApifyResponse.class,
                getPlacesQueryParams(latitude, longitude, radius));
    }

    private Map<String, Object> getWeatherQueryParams(float latitude, float longitude, LocalDate startDate,
            LocalDate endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("daily", new String[] { "rain_sum", "temperature_2m_min", "temperature_2m_max" });
        params.put("hourly", new String[] { "temperature_2m", "rain", "wind_speed_10m" });
        return params;
    }

    private Map<String, Object> getPlacesQueryParams(float latitude, float longitude, int radius) {
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", "API-KEY");
        params.put("categories", new String[] { "activity", "catering", "entertainment", "rental", "tourism",
                "camping", "beach", "sport" });
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("radius", radius);
        return params;
    }

    private String buildWeatherUrlTemplate() {
        return UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("latitude", "{latitude}")
                .queryParam("longitude", "{longitude}")
                .queryParam("start_date", "{start_date}")
                .queryParam("end_date", "{end_date}")
                .queryParam("daily", "{daily}")
                .queryParam("hourly", "{hourly}")
                .encode()
                .toUriString();
    }

    private String buildPlacesUrlTemplate() {
        return UriComponentsBuilder.fromHttpUrl(GEOAPIFY_API_URL)
                .queryParam("apiKey", "{apiKey}")
                .queryParam("categories", "{categories}")
                .queryParam("filter", "circle:{longitude},{latitude},{radius}")
                .queryParam("bias", "proximity:{longitude},{latitude}")
                .encode()
                .toUriString();
    }
}
