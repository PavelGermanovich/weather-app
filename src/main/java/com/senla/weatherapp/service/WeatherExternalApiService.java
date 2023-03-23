package com.senla.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.weatherapp.dto.WeatherDto;
import com.senla.weatherapp.exception.WeatherDataRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class WeatherExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherExternalApiService.class);

    @Value("${app.weather.fetch.key}")
    private String apiKey;
    @Value("${app.weather.api.external.url}")
    private String externalApiUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherDto fetchWeatherDataFromExternalAPI(String country) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", apiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(externalApiUrl + "/current.json")
                .queryParam("q", country);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        if (response.getBody() != null) {
            try {
                return new ObjectMapper().readerFor(WeatherDto.class).readValue(response.getBody());
            } catch (JsonProcessingException e) {
                logger.error("Error deserializing response body from External Weather API: {}", e.getMessage());
                throw new WeatherDataRetrievalException("Error processing weather data for the following country" + country, e);
            }
        } else {
            logger.warn("Received null response body from External Weather API");
            throw new WeatherDataRetrievalException("Received null response body from API");
        }
    }
}
