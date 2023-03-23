package com.senla.weatherapp.repository;

import com.senla.weatherapp.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrentWeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findByLocation_NameAndWeatherDateIsBetween(String location, LocalDate from, LocalDate to);
    Optional<Weather> findFirstByLocation_NameOrderByIdDesc(String name);
}
