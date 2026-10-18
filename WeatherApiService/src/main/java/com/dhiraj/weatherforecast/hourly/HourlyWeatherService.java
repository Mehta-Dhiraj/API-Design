package com.dhiraj.weatherforecast.hourly;

import com.dhiraj.weatherapicommon.HourlyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherforecast.location.LocationNotFoundException;
import com.dhiraj.weatherforecast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HourlyWeatherService {

    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return hourlyWeatherRepository.findByLocationCode(locationInDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
        Location locationInDB = locationRepository.findByCode(locationCode);

        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }

        return hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeatherInRequest)
            throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }

        for (HourlyWeather item : hourlyWeatherInRequest) {
            item.getId().setLocation(location);
        }

        List<HourlyWeather> hourlyWeatherInDB = location.getHourlyWeatherList();
        List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();

        for (HourlyWeather item : hourlyWeatherInDB) {
            if (!hourlyWeatherInRequest.contains(item)) {
                hourlyWeatherToBeRemoved.add(item.getShallowCopy());
            }
        }

        for (HourlyWeather item : hourlyWeatherToBeRemoved) {
            hourlyWeatherInDB.remove(item);
        }

        return (List<HourlyWeather>) hourlyWeatherRepository.saveAll(hourlyWeatherInRequest);
    }
}
