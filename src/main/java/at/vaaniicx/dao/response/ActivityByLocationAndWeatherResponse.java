package at.vaaniicx.dao.response;

import at.vaaniicx.dao.response.activity.GeoApifyResponse;
import at.vaaniicx.dao.response.weather.Weather;

public record ActivityByLocationAndWeatherResponse(Weather weather, GeoApifyResponse place) {
}
