package com.senla.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AverageWeatherDataResponse {
    @JsonProperty("average_temp")
    private double averageTemp;
    @JsonProperty("average_wind_speed_m_per_hour")
    private double averageWindSpeed;
    @JsonProperty("average_humidity")
    private double averageHumidity;
    @JsonProperty("average_pressure")
    private double pressureMb;
}
