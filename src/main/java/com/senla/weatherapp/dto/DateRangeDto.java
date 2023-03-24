package com.senla.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeDto {
    @NotNull(message = "From date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate from;

    @NotNull(message = "To date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate to;
}
