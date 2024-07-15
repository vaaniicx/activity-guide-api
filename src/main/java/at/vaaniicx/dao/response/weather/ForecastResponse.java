package at.vaaniicx.dao.response.weather;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class ForecastResponse {
    @JsonProperty
    private float latitude;

    @JsonProperty
    private float longitude;

    @JsonProperty("time")
    private LocalDateTime[] dates;

    @JsonProperty("temperature_2m")
    private double[] temperatures;

    @JsonProperty("rain_sum")
    private double rainSum;

    @JsonProperty("rain")
    private double[] rain;

    @JsonProperty("wind_speed_10m")
    private double[] windSpeed;

    @JsonProperty("temperature_2m_min")
    private double minimumTemperature;

    @JsonProperty("temperature_2m_max")
    private double maximumTemperature;

    @JsonProperty("hourly")
    private void unpackHourly(Map<String, String[]> hourly) {
        this.dates = Arrays.stream(hourly.get("time")).map(LocalDateTime::parse).toArray(LocalDateTime[]::new);
        this.temperatures =
                Arrays.stream(hourly.get("temperature_2m")).mapToDouble(Double::parseDouble).toArray();
        this.windSpeed = Arrays.stream(hourly.get("wind_speed_10m")).mapToDouble(Double::parseDouble).toArray();
        this.rain = Arrays.stream(hourly.get("rain")).mapToDouble(Double::parseDouble).toArray();
    }

    @JsonProperty("daily")
    private void unpackDaily(Map<String, String[]> daily) {
        this.rainSum = Double.parseDouble(daily.get("rain_sum")[0]);
        this.minimumTemperature = Double.parseDouble(daily.get("temperature_2m_min")[0]);
        this.maximumTemperature = Double.parseDouble(daily.get("temperature_2m_max")[0]);
    }
}