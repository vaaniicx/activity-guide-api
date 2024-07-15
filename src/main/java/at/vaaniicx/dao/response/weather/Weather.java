package at.vaaniicx.dao.response.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Weather(@JsonProperty("min_temperature") double minimumTemperature,
                      @JsonProperty("max_temperature") double maximumTemperature,
                      @JsonProperty("rain_sum") double rainSum,
                      @JsonProperty("") Hourly[] hourly) {
}
