package com.senla.weatherapp.scheduledtask;

import com.senla.weatherapp.converter.WeatherConverter;
import com.senla.weatherapp.dto.WeatherDto;
import com.senla.weatherapp.entity.Weather;
import com.senla.weatherapp.exception.WeatherDataRetrievalException;
import com.senla.weatherapp.service.WeatherExternalApiService;
import com.senla.weatherapp.service.WeatherService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherScheduleUpdate {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherService weatherService;
    private final WeatherExternalApiService weatherExternalApiService;
    private final WeatherConverter weatherConverter;
    @Value("${app.weather.city:Minsk}")
    private String city;

    @Autowired
    public WeatherScheduleUpdate(WeatherService weatherService, WeatherExternalApiService weatherExternalApiService, WeatherConverter weatherConverter) {
        this.weatherService = weatherService;
        this.weatherExternalApiService = weatherExternalApiService;
        this.weatherConverter = weatherConverter;
    }

    @Scheduled(fixedRateString = "${app.weather.fetch.rate}")
    @Transactional
    public void scheduledWeatherUpdate() {
        try {
            WeatherDto weatherDto = weatherExternalApiService.fetchWeatherDataFromExternalAPI(city);
            Weather weather = weatherConverter.convertToEntity(weatherDto);
            weatherService.saveWeatherData(weather);
        } catch (WeatherDataRetrievalException e) {
            logger.error("Weather retrieval exception occurs, data not fetched from External API service", e);
        }
    }
}
