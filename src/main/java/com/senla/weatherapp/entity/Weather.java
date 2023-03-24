package com.senla.weatherapp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "weather")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private double temperature;
    private double windSpeedMtrHr;
    private double pressureMb;
    private int humidity;
    private String weatherCondition;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location")
    private Location location;
    private LocalDate weatherDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Double.compare(weather.temperature, temperature) == 0 && Double
                .compare(weather.windSpeedMtrHr, windSpeedMtrHr) == 0 && Double
                .compare(weather.pressureMb, pressureMb) == 0 && humidity == weather.humidity && id
                .equals(weather.id) && weatherCondition.equals(weather.weatherCondition) && location
                .equals(weather.location) && weatherDate.equals(weather.weatherDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperature, windSpeedMtrHr, pressureMb, humidity, weatherCondition,
                location, weatherDate);
    }
}
