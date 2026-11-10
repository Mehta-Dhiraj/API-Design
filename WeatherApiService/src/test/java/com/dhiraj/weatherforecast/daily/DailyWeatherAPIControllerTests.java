package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherforecast.location.LocationNotFoundException;
import com.dhiraj.weatherforecast.realtime.GeolocationException;
import com.dhiraj.weatherforecast.realtime.GeolocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailyWeatherAPIController.class)
public class DailyWeatherAPIControllerTests {

    private static final String END_POINT_PATH = "/v1/daily";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DailyWeatherService dailyWeatherService;

    @MockitoBean
    private GeolocationService geolocationService;

    @Test
    public void testGetByIpShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        GeolocationException ex = new GeolocationException("GeoLocation Error");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByIpShouldReturn404NotFound() throws Exception {
        Location location = new Location().code("DELHI_IN");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

        LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
        Mockito.when(dailyWeatherService.findByLocation(location)).thenThrow(ex);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
                .andDo(print());
    }

    @Test
    public void testGetByIpShouldReturn204NoContent() throws Exception {
        Location location = new Location().code("DELHI_IN");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(dailyWeatherService.findByLocation(location)).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetByIpShouldReturn200OK() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");

        DailyWeather forecast1 = new DailyWeather()
                .location(location)
                .dayOfMonth(16)
                .month(7)
                .minTemp(23)
                .maxTemp(32)
                .precipitation(40)
                .status("Cloudy");

        DailyWeather forecast2 = new DailyWeather()
                .location(location)
                .dayOfMonth(17)
                .month(7)
                .minTemp(25)
                .maxTemp(34)
                .precipitation(30)
                .status("Sunny");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(dailyWeatherService.findByLocation(location)).thenReturn(List.of(forecast1, forecast2));

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.location", is(location.toString())))
                .andExpect(jsonPath("$.daily_forecast[0].day_of_month", is(16)))
                .andExpect(jsonPath("$.daily_forecast[0].min_temp", is(23)))
                .andExpect(jsonPath("$.daily_forecast[0].max_temp", is(32)))
                .andDo(print());
    }
}
