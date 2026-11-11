package com.dhiraj.weatherforecast.realtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"location", "temperature", "humidity", "precipitation", "wind_speed", "status", "last_updated"})
public class RealtimeWeatherDTO {

    private String location;

    @Range(min = -50, max = 50, message = "Temperature must be in range of -50 and 50 celsius degree")
    private int temperature;

    @Range(min = 0, max = 100, message = "humidity must be in range of 0 and 100 percentage")
    private int humidity;

    @Range(min = 0, max = 100, message = "Precipitation must be in range of 0 and 100 percentage")
    private int precipitation;

    @JsonProperty("wind_speed")
    @Range(min = 0, max = 200, message = "Wind Speed must be in range of 0 and 200 km/h")
    private int windSpeed;

    @NotBlank(message = "Status must not be empty")
    @Length(min = 3, max = 50, message = "Status must be between 3-50 characters")
    private String status;

    @JsonProperty("last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date lastUpdated;
}
