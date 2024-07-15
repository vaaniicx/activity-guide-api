package at.vaaniicx.mapper;

import java.time.LocalDateTime;

import at.vaaniicx.dao.response.weather.ForecastResponse;
import at.vaaniicx.dao.response.weather.Hourly;
import at.vaaniicx.dao.response.weather.Weather;

public class WeatherResponseMapper {
    public static Weather map(ForecastResponse response) {
        return new Weather(response.getMinimumTemperature(), response.getMaximumTemperature(), response.getRainSum(),
                mapHourly(response.getDates(), response.getTemperatures(), response.getRain(),
                        response.getWindSpeed()));
    }

    private static Hourly[] mapHourly(LocalDateTime[] dates, double[] temperatures, double[] rain, double[] windSpeed) {
        Hourly[] hourly = new Hourly[dates.length];
        for (int i = 0; i < dates.length; i++) {
            hourly[i] = new Hourly(extractTimeFromDate(dates[i]), temperatures[i], rain[i], windSpeed[i]);
        }
        return hourly;
    }

    private static int extractTimeFromDate(LocalDateTime dateTime) {
        return dateTime.getHour();
    }
}
