package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.DailyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("""
            SELECT d FROM DailyWeather d WHERE
            d.id.location.code = ?1 and d.id.location.trashed = false
            """)
    List<DailyWeather> findByLocationCode(String locationCode);
}
