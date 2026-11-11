package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherforecast.BadRequestException;
import com.dhiraj.weatherforecast.CommonUtility;
import com.dhiraj.weatherforecast.realtime.GeolocationException;
import com.dhiraj.weatherforecast.realtime.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/daily")
@Validated
public class DailyWeatherAPIController {

    private DailyWeatherService dailyWeatherService;
    private GeolocationService geolocationService;
    private ModelMapper modelMapper;

    public DailyWeatherAPIController(DailyWeatherService dailyWeatherService, GeolocationService geolocationService,
                                     ModelMapper modelMapper) {
        super();
        this.dailyWeatherService = dailyWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listDailyForeCastByIPAddress(HttpServletRequest request) throws GeolocationException {

        String ipAddress = CommonUtility.getIPAddress(request);

        Location location = geolocationService.getLocation(ipAddress);
        List<DailyWeather> dailyWeatherList = dailyWeatherService.findByLocation(location);

        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntityToDTO(dailyWeatherList));

    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listDailyForecastByLocationCode(@PathVariable("locationCode") String locationCode) {
        List<DailyWeather> dailyWeatherList = dailyWeatherService.findByLocationCode(locationCode);

        if (dailyWeatherList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntityToDTO(dailyWeatherList));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateDailyForecast(@PathVariable("locationCode") String locationCode,
                                                 @RequestBody @Valid List<DailyWeatherDTO> listDTO) throws BadRequestException {
        if (listDTO.isEmpty()) {
            throw new BadRequestException("Daily forecast data cannot be empty");
        }

        List<DailyWeather> dailyWeather = listDTO2ListEntity(listDTO);
        List<DailyWeather> updatedForecast = dailyWeatherService.updateByLocationCode(locationCode, dailyWeather);

        return ResponseEntity.ok(listEntityToDTO(updatedForecast));
    }

    private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
        List<DailyWeather> listEntity = new ArrayList<>();

        listDTO.forEach(dto -> listEntity.add(modelMapper.map(dto, DailyWeather.class)));

        return listEntity;
    }

    private DailyWeatherListDTO listEntityToDTO(List<DailyWeather> dailyWeatherList) {
        Location location = dailyWeatherList.get(0).getId().getLocation();

        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        dailyWeatherList.forEach(dailyWeather -> listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class)));
        return listDTO;
    }
}
