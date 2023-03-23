package com.senla.weatherapp.service;

import com.senla.weatherapp.dto.DateRangeDto;
import com.senla.weatherapp.entity.Weather;
import com.senla.weatherapp.repository.CurrentWeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {
    private final CurrentWeatherRepository weatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    public WeatherService(CurrentWeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void saveWeatherData(Weather weather) {
        weatherRepository.save(weather);
        logger.info("weather information is saved in DB");
    }

    public Optional<Weather> getCurrentWeather(String city) {
        return weatherRepository.findFirstByLocation_NameOrderByIdDesc(city);
    }

    public List<Weather> getAverageWeatherData(String city, DateRangeDto dateRangeDto) {
       return weatherRepository
                .findByLocation_NameAndWeatherDateIsBetween(city, dateRangeDto.getFrom(), dateRangeDto.getTo());
    }
}
