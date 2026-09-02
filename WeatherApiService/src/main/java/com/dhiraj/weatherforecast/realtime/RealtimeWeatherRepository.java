package com.dhiraj.weatherforecast.realtime;

import com.dhiraj.weatherapicommon.RealtimeWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

    @Query("SELECT r FROM RealtimeWeather r where r.location.countryCode=?1 and r.location.cityName = ?2")
    RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);

    @Query("SELECT r FROM RealtimeWeather r where r.locationCode=?1 AND r.location.trashed = false")
    RealtimeWeather findByLocationCode(String locationCode);

}
