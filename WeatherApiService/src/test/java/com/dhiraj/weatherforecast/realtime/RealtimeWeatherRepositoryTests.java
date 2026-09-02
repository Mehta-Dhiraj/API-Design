package com.dhiraj.weatherforecast.realtime;

import com.dhiraj.weatherapicommon.RealtimeWeather;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {

    @Autowired
    private RealtimeWeatherRepository repo;

    @Test
    public void testUpdate() {

        String locationCode = "NYC_USA";


        RealtimeWeather realtimeWeather = repo.findById(locationCode).get();

        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());

        RealtimeWeather updatedRealTimeWeather = repo.save(realtimeWeather);

        assertThat(updatedRealTimeWeather.getHumidity()).isEqualTo(32);


    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
        String countryCode = "JP";
        String cityName = "Tokyo";

        RealtimeWeather weather = repo.findByCountryCodeAndCity(countryCode, cityName);

        assertThat(weather).isNull();

    }

    @Test
    public void testFindByCountryCodeAndCityFound() {
        String countryCode = "US";
        String cityName = "New York City";

        RealtimeWeather weather = repo.findByCountryCodeAndCity(countryCode, cityName);

        assertThat(weather).isNotNull();
        assertThat(weather.getLocation().getCityName()).isEqualTo(cityName);
    }

    @Test
    public void testFindByLocationNotFound() {
        String locationCode = "ABCXYZ";
        RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByTrashedLocationNotFound() {
        String locationCode = "NYC_USA11";
        RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByLocationFound() {
        String locationCode = "DELHI_IN";
        RealtimeWeather realtimeWeather = repo.findByLocationCode(locationCode);

        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocationCode()).isEqualTo(locationCode);
    }
}
