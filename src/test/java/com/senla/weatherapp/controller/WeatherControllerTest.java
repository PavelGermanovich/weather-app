package com.senla.weatherapp.controller;

import com.senla.weatherapp.converter.WeatherConverter;
import com.senla.weatherapp.dto.AverageWeatherDataResponse;
import com.senla.weatherapp.dto.DateRangeDto;
import com.senla.weatherapp.dto.ResponseMessageDto;
import com.senla.weatherapp.entity.Location;
import com.senla.weatherapp.entity.Weather;
import com.senla.weatherapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherControllerTest {
    private final WeatherService weatherService = Mockito.mock(WeatherService.class);
    private final WeatherConverter weatherConverter = Mockito.mock(WeatherConverter.class);
    private final WeatherController weatherController = new WeatherController(weatherService, weatherConverter);
    private final String LOCATION = "Minsk";

    @BeforeEach
    public void before() {
        ReflectionTestUtils.setField(weatherController, "defaultCity", LOCATION);
    }

    @Test
    public void testGetCurrentWeatherExist() {
        Location minskLocation = new Location();
        minskLocation.setName(LOCATION);
        minskLocation.setId(1L);
        Weather weather = Weather.builder().id(1L).temperature(22.0).windSpeedMtrHr(33.0).pressureMb(44.0)
                .humidity(10).weatherCondition("Sunny").location(minskLocation).build();
        Mockito.when(weatherService.getCurrentWeather(LOCATION)).thenReturn(Optional.of(weather));
        ResponseEntity<?> responseEntity = weatherController.getCurrentWeather();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetCurrentWeatherNotExist() {
        Mockito.when(weatherService.getCurrentWeather(LOCATION)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = weatherController.getCurrentWeather();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ResponseMessageDto errorDto = new ResponseMessageDto("Weather data not found in DB for the city " + LOCATION);
        assertEquals(errorDto, responseEntity.getBody());
    }

    @Test
    public void testCalculateAverageTemperatureData() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Location minskLocation = new Location();
        minskLocation.setName(LOCATION);
        minskLocation.setId(1L);
        Weather weatherFirst = Weather.builder().id(1L).temperature(22.0).windSpeedMtrHr(33.0).pressureMb(44.0)
                .humidity(10).weatherCondition("Sunny").location(minskLocation)
                .weatherDate(LocalDate.parse("22-11-2023", dateTimeFormatter)).build();
        Weather weatherSecond = Weather.builder().id(2L).temperature(24.0).windSpeedMtrHr(44.0).pressureMb(55.0)
                .humidity(22).weatherCondition("Sunny").location(minskLocation)
                .weatherDate(LocalDate.parse("23-11-2023", dateTimeFormatter)).build();

        DateRangeDto dateRangeDto = new DateRangeDto(LocalDate.of(2023, 11, 21), LocalDate.of(2023, 11, 24));
        Mockito.when(weatherService.getAverageWeatherData(LOCATION, dateRangeDto))
                .thenReturn(List.of(weatherFirst, weatherSecond));
        ResponseEntity<?> responseEntity = weatherController.calculateAverageTemperatureData(dateRangeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AverageWeatherDataResponse weatherData = ((AverageWeatherDataResponse) responseEntity.getBody());
        assertEquals((weatherFirst.getTemperature() + weatherSecond.getTemperature()) / 2,
                weatherData.getAverageTemp(), "Average Temp is incorrect");
        assertEquals(((double) weatherFirst.getHumidity() + weatherSecond.getHumidity()) / 2,
                weatherData.getAverageHumidity(), "Average Humidity is incorrect");
        assertEquals((weatherFirst.getPressureMb() + weatherSecond.getPressureMb()) / 2,
                weatherData.getPressureMb(), "Average Humidity is incorrect");
        assertEquals((weatherFirst.getWindSpeedMtrHr() + weatherSecond.getWindSpeedMtrHr()) / 2,
                weatherData.getAverageWindSpeed(), "Average Temp is incorrect");
    }

    @Test
    public void testCalculateAverageTemperature_NoDataForSpecifiedRange() {
        DateRangeDto dateRange = new DateRangeDto(LocalDate.of(2011, 3, 11), LocalDate.of(2012, 2, 1));
        Mockito.when(weatherService.getAverageWeatherData(LOCATION, dateRange))
                .thenReturn(List.of());

        ResponseEntity<?> responseEntity = weatherController.calculateAverageTemperatureData(dateRange);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ResponseMessageDto responseMessageDto = new ResponseMessageDto("No weather data found for specified date range");
        assertEquals(responseEntity.getBody(), responseMessageDto);
    }

    @Test
    public void testCalculateAverageIncorrectDateRangeFromGreaterThanTo() {
        DateRangeDto dateRange = new DateRangeDto(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 2, 1));
        ResponseEntity<?> response = weatherController.calculateAverageTemperatureData(dateRange);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response is incorrect");
        assertEquals(response.getBody(), new ResponseMessageDto("The 'from' date must be before the 'to' date."));
    }
}
