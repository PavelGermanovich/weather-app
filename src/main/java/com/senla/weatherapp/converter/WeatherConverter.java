package com.senla.weatherapp.converter;

import com.senla.weatherapp.dto.WeatherDto;
import com.senla.weatherapp.entity.Weather;
import com.senla.weatherapp.entity.Location;
import com.senla.weatherapp.repository.LocationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherConverter {
    private final LocationRepository locationRepository;

    @Autowired
    public WeatherConverter(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Weather convertToEntity(WeatherDto weatherDto) {
        Weather weather = new Weather();
        BeanUtils.copyProperties(weatherDto, weather);

        Location location = locationRepository.findByName(weatherDto.getLocation());
        if (location != null) {
            weather.setLocation(location);
        } else {
            location = new Location();
            location.setName(weatherDto.getLocation());
            weather.setLocation(location);
        }

        return weather;
    }

    public WeatherDto convertToDto(Weather weather) {
        WeatherDto weatherDto = new WeatherDto();
        BeanUtils.copyProperties(weather, weatherDto);
        weatherDto.setLocation(weather.getLocation().getName());
        return weatherDto;
    }
}