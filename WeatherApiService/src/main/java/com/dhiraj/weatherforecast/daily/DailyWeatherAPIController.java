package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherforecast.CommonUtility;
import com.dhiraj.weatherforecast.realtime.GeolocationException;
import com.dhiraj.weatherforecast.realtime.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/daily")
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

    private DailyWeatherListDTO listEntityToDTO(List<DailyWeather> dailyWeatherList) {
        Location location = dailyWeatherList.get(0).getId().getLocation();

        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        dailyWeatherList.forEach(dailyWeather -> listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class)));
        return listDTO;
    }
}
