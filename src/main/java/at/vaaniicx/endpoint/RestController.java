package at.vaaniicx.endpoint;

import at.vaaniicx.dao.response.ActivityByLocationAndWeatherDayResponse;
import at.vaaniicx.dao.response.activity.GeoApifyResponse;
import at.vaaniicx.dao.response.weather.ForecastResponse;
import at.vaaniicx.dao.response.weather.Hourly;
import at.vaaniicx.dao.response.weather.Weather;
import at.vaaniicx.mapper.WeatherResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/results")
public class RestController {
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";

    private static final String GEOAPIFY_API_URL = "https://api.geoapify.com/v2/places";

    @GetMapping
    public ResponseEntity<List<ActivityByLocationAndWeatherDayResponse>> getActivityByLocationAndWeather(
            @RequestParam(name = "latitude") float latitude,
            @RequestParam(name = "longitude") float longitude, @RequestParam(name = "radius") int radius,
            @RequestParam(name = "start") LocalDate startDate,
            @RequestParam(name = "end") LocalDate endDate) {

        Weather weatherResponse = WeatherResponseMapper.map(Objects.requireNonNull(
                new RestTemplate().getForEntity(buildWeatherUrlTemplate(), ForecastResponse.class,
                        getWeatherQueryParams(latitude, longitude, startDate, endDate)).getBody()));
        GeoApifyResponse geoApifyResponse = new RestTemplate().getForEntity(buildPlacesUrlTemplate(),
                GeoApifyResponse.class, getPlacesQueryParams(latitude, longitude, radius)).getBody();


        List<ActivityByLocationAndWeatherDayResponse> result = new ArrayList<>();
        int dayCount = 0;
        for (LocalDate date : startDate.datesUntil(endDate).toList()) {
            Hourly[] hourly = new Hourly[24];
            for (int i = 0; i < 24; i++) {
                hourly[i] = weatherResponse.hourly()[i + (24 * dayCount)];
            }
            dayCount++;
            result.add(new ActivityByLocationAndWeatherDayResponse(
                    date,
                    new Weather(weatherResponse.minimumTemperature(), weatherResponse.maximumTemperature(), weatherResponse.rainSum(), hourly),
                    geoApifyResponse.getPlaces())
            );
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, Object> getWeatherQueryParams(float latitude, float longitude, LocalDate startDate,
                                                      LocalDate endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("daily", new String[]{"rain_sum", "temperature_2m_min", "temperature_2m_max"});
        params.put("hourly", new String[]{"temperature_2m", "rain", "wind_speed_10m"});
        return params;
    }

    private Map<String, Object> getPlacesQueryParams(float latitude, float longitude, int radius) {
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", "");
        params.put("categories", new String[]{"activity", "catering", "entertainment", "rental", "tourism",
                "camping", "beach", "sport"});
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
