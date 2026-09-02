package com.dhiraj.weatherforecast.realtime;

import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherapicommon.RealtimeWeather;
import com.dhiraj.weatherforecast.CommonUtility;
import com.dhiraj.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/realtime")
public class RealtimeWeatherApiController {

    private RealtimeWeatherService realtimeWeatherService;
    private GeolocationService geolocationService;
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(RealtimeWeatherApiController.class);

    public RealtimeWeatherApiController(RealtimeWeatherService realtimeWeatherService,
                                        GeolocationService geolocationService, ModelMapper modelMapper) {
        this.realtimeWeatherService = realtimeWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location location = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(location);
            return ResponseEntity.ok(entity2DTO(realtimeWeather));
        } catch (GeolocationException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {

        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
            return ResponseEntity.ok(entity2DTO(realtimeWeather));
        } catch (LocationNotFoundException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode, @RequestBody @Valid RealtimeWeather realtimeWeather) {
        realtimeWeather.setLocationCode(locationCode);
        try {
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
            return ResponseEntity.ok(entity2DTO(updatedRealtimeWeather));
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather){
        return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }
}
