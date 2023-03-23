package com.senla.weatherapp.exception;

public class WeatherDataRetrievalException extends RuntimeException{
    public WeatherDataRetrievalException() {
    }

    public WeatherDataRetrievalException(String message) {
        super(message);
    }

    public WeatherDataRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
