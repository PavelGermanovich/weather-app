package com.senla.weatherapp.exception;

import com.senla.weatherapp.controller.WeatherController;
import com.senla.weatherapp.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice(assignableTypes = WeatherController.class)
public class CustomExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessageDto> handleDateTimeParseException(DateTimeParseException ex) {
        String message = "Invalid date format, should be used dd-MM-yyyy";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(message));
    }
}

