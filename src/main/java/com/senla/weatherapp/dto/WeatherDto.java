package com.senla.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherDto {
    private double temperature;
    private double windSpeedMtrHr;
    private double pressureMb;
    private int humidity;
    private String weatherCondition;
    private String location;
    @JsonIgnore
    private LocalDate weatherDate;

    @JsonProperty("location")
    private void unpackNestedLocation(Map<String, String> location) {
        this.location = location.get("name");
    }

    @JsonProperty("current")
    private void unpackNestedCurrentWeather(Map<String, Object> currentWeather) {
        this.weatherCondition = ((Map<String, String>) currentWeather.get("condition")).get("text");
        this.temperature = (Double) currentWeather.get("temp_c");
        this.humidity = (Integer) currentWeather.get("humidity");
        this.pressureMb = (Double) currentWeather.get("pressure_mb");
        this.windSpeedMtrHr = ((Double) currentWeather.get("wind_kph")) * 1000;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.weatherDate = LocalDateTime.parse((String) currentWeather.get("last_updated"), formatter).toLocalDate();
    }
}
