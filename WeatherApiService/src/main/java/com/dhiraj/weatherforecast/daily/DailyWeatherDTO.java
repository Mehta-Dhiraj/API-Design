package com.dhiraj.weatherforecast.daily;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {

    @JsonProperty("day_of_month")
    private int dayOfMonth;

    private int month;

    @JsonProperty("min_temp")
    private int minTemp;

    @JsonProperty("max_temp")
    private int maxTemp;

    private int precipitation;

    private String status;
}
