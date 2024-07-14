package at.vaaniicx.endpoint;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/result")
public class RestController {

    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";

    @GetMapping
    public ResponseEntity<String> findByLocation(
            @RequestParam(name = "latitude") float latitude,
            @RequestParam(name = "longitude") float longitude, @RequestParam(name = "radius") int radius,
            @RequestParam(name = "start_date") LocalDate startDate,
            @RequestParam(name = "end_date") LocalDate endDate) {

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("latitude", "{latitude}")
                .queryParam("longitude", "{longitude}")
                .queryParam("start_date", "{start_date}")
                .queryParam("end_date", "{end_date}")
                .queryParam("daily", "{daily}")
                .encode()
                .toUriString();

        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("daily", new String[] { "temperature_2m_min", "temperature_2m_max" });

        return new RestTemplate().getForEntity(urlTemplate, String.class, params);
    }
}
