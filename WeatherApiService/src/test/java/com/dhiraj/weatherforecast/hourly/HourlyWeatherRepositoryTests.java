package com.dhiraj.weatherforecast.hourly;

import com.dhiraj.weatherapicommon.HourlyWeather;
import com.dhiraj.weatherapicommon.HourlyWeatherId;
import com.dhiraj.weatherapicommon.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {

    @Autowired
    private HourlyWeatherRepository repo;

    @Test
    public void testAdd() {
        String locationCode = "DELHI_IN";
        int hourOfDay = 12;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather()
                .location(location)
                .hourOfDay(hourOfDay)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeather updatedHourlyWeather = repo.save(forecast);

        assertThat(updatedHourlyWeather.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedHourlyWeather.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete() {
        Location location = new Location().code("DELHI_IN");
        HourlyWeatherId id = new HourlyWeatherId(10, location);
        repo.deleteById(id);
        Optional<HourlyWeather> result = repo.findById(id);
        assertThat(result).isNotPresent();
    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "DELHI_IN";
        int currentHour = 10;

        List<HourlyWeather> hourlyForecast = repo.findByLocationCode(locationCode, currentHour);

        assertThat(hourlyForecast).isNotEmpty();
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "MBMH_IN";
        int currentHour = 6;

        List<HourlyWeather> hourlyForecast = repo.findByLocationCode(locationCode, currentHour);

        assertThat(hourlyForecast).isEmpty();
    }

}
