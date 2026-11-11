package com.dhiraj.weatherforecast.daily;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.DailyWeatherId;
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
public class DailyWeatherRepositoryTests {

    @Autowired
    private DailyWeatherRepository repository;

    @Test
    public void testAddSuccess() {
        Location location = new Location().code("NYC_USA2");

        DailyWeather forecast = new DailyWeather()
                .location(location)
                .dayOfMonth(16)
                .month(7)
                .minTemp(23)
                .maxTemp(32)
                .precipitation(40)
                .status("Cloudy");

        DailyWeather savedDailyWeather = repository.save(forecast);

        assertThat(savedDailyWeather).isNotNull();
        assertThat(savedDailyWeather.getId().getLocation().getCode()).isEqualTo("NYC_USA2");

    }

    @Test
    public void testDelete() {
        Location location = new Location().code("DELHI_IN");
        DailyWeatherId id = new DailyWeatherId(16, 7, location);

        repository.deleteById(id);

        Optional<DailyWeather> result = repository.findById(id);
        assertThat(result).isNotPresent();

    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "DELHI_IN";

        List<DailyWeather> dailyWeather = repository.findByLocationCode(locationCode);

        assertThat(dailyWeather).isNotEmpty();

    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "DELHI_IN1";

        List<DailyWeather> dailyWeather = repository.findByLocationCode(locationCode);

        assertThat(dailyWeather).isEmpty();
    }
}
