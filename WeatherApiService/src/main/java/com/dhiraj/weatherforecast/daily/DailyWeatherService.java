package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherforecast.location.LocationNotFoundException;
import com.dhiraj.weatherforecast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyWeatherService {

    private DailyWeatherRepository dailyWeatherRepo;
    private LocationRepository locationRepo;

    public DailyWeatherService(DailyWeatherRepository dailyWeatherRepo, LocationRepository locationRepo) {
        super();
        this.dailyWeatherRepo = dailyWeatherRepo;
        this.locationRepo = locationRepo;
    }

    public List<DailyWeather> findByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return dailyWeatherRepo.findByLocationCode(locationInDB.getCode());

    }
}
