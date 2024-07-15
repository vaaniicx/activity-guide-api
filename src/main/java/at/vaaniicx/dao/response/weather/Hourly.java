package at.vaaniicx.dao.response.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Hourly(int hour, double temperature, double rain, @JsonProperty("wind_speed") double windSpeed) {
}
