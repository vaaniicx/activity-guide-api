package at.vaaniicx.dao.response.weather;

public record Hourly(int hour, double temperature, double rain, double windSpeed) {
}
