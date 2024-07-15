package at.vaaniicx.dao.response;

import at.vaaniicx.dao.response.activity.GeoApifyFeature;
import at.vaaniicx.dao.response.weather.Weather;

import java.time.LocalDate;

public record ActivityByLocationAndWeatherDayResponse(LocalDate date, Weather weather, GeoApifyFeature[] places) {

}
