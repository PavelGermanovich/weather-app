package com.senla.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeDto {
    @NotNull(message = "From date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate from;

    @NotNull(message = "To date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate to;
}
