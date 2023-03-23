package com.senla.weatherapp.controller;

import com.senla.weatherapp.converter.WeatherConverter;
import com.senla.weatherapp.dto.AverageWeatherDataResponse;
import com.senla.weatherapp.dto.DateRangeDto;
import com.senla.weatherapp.dto.ResponseMessageDto;
import com.senla.weatherapp.entity.Weather;
import com.senla.weatherapp.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherConverter weatherConverter;

    @Value("${app.weather.city:Minsk}")
    private String defaultCity;

    @Autowired
    public WeatherController(WeatherService weatherService, WeatherConverter weatherConverter) {
        this.weatherService = weatherService;
        this.weatherConverter = weatherConverter;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWeather() {
        Optional<Weather> lastUpdatedWeather = weatherService.getCurrentWeather(defaultCity);
        if (lastUpdatedWeather.isPresent()) {
            return ResponseEntity.ok(weatherConverter.convertToDto(lastUpdatedWeather.get()));
        } else {
            ResponseMessageDto errorDto = new ResponseMessageDto("Weather data not found in DB for the city " + defaultCity);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
        }
    }

    @PostMapping("/average")
    public ResponseEntity<?> calculateAverageTemperatureData(@RequestBody @Valid DateRangeDto dateRange) {
        if (dateRange.getFrom().isAfter(dateRange.getTo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessageDto("The 'from' date must be before the 'to' date."));
        }

        List<Weather> weatherList = weatherService.getAverageWeatherData(defaultCity, dateRange);
        if (weatherList.isEmpty()) {
            ResponseMessageDto responseMessageDto = new ResponseMessageDto("No weather data found for specified date range");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessageDto);
        } else {
            AverageWeatherDataResponse dataResponse = new AverageWeatherDataResponse();
            dataResponse.setAverageTemp(weatherList.stream().mapToDouble(Weather::getTemperature).average().getAsDouble());
            dataResponse.setAverageWindSpeed(weatherList.stream().mapToDouble(Weather::getWindSpeedMtrHr).average().getAsDouble());
            dataResponse.setAverageHumidity(weatherList.stream().mapToDouble(Weather::getHumidity).average().getAsDouble());
            dataResponse.setPressureMb(weatherList.stream().mapToDouble(Weather::getPressureMb).average().getAsDouble());
            return ResponseEntity.ok(dataResponse);
        }
    }
}
