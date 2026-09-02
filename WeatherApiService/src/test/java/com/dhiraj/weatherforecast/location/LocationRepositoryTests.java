package com.dhiraj.weatherforecast.location;

import com.dhiraj.weatherapicommon.HourlyWeather;
import com.dhiraj.weatherapicommon.Location;
import com.dhiraj.weatherapicommon.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testAddSuccess() {
        Location location = new Location();
        location.setCode("MBMH_IN");
        location.setCityName("Mumbai");
        location.setCountryName("India");
        location.setCountryCode("IN");
        location.setRegionName("Maharashtra");
        location.setEnabled(true);

        Location savedLocation = locationRepository.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("MBMH_IN");

    }

    @Test
    public void testListSuccess() {
        List<Location> locations = locationRepository.findUnTrashed();
        assertThat(locations).isNotNull();
        assertThat(locations.size()).isGreaterThan(0);
        locations.forEach(System.out::println);
    }

    @Test
    public void testGetFound() {
        String code = "NYC_USA";

        Location location = locationRepository.findByCode(code);
        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    public void testGetNotFound() {
        String code = "ABCD";

        Location location = locationRepository.findByCode(code);
        assertThat(location).isNull();
    }

    @Test
    public void testDeleteSuccess() {
        String code = "ABCD";
        locationRepository.trashByCode(code);
    }

    @Test
    public void testRealtimeWeatherDate() {
        String code = "NYC_USA";

        Location location = locationRepository.findByCode(code);

        RealtimeWeather realtimeWeather = location.getRealtimeWeather();

        if (realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(location);
            location.setRealtimeWeather(realtimeWeather);
        }
        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(60);
        realtimeWeather.setPrecipitation(70);
        realtimeWeather.setStatus("Sunny");
        realtimeWeather.setWindSpeed(10);
        realtimeWeather.setLastUpdated(new Date());

        Location updateLocation = locationRepository.save(location);
        assertThat(updateLocation.getRealtimeWeather().getLocationCode()).isEqualTo(location.getCode());
    }

    @Test
    public void testAddHourlyWeatherData() {
        Location location = locationRepository.findById("DELHI_IN").get();
        List<HourlyWeather> hourlyWeatherList = location.getHourlyWeatherList();

        HourlyWeather hourlyWeather1 = new HourlyWeather()
                .id(location, 10)
                .temperature(15)
                .precipitation(50)
                .status("Cloudy");

        HourlyWeather hourlyWeather2 = new HourlyWeather()
                .location(location)
                .hourOfDay(11)
                .temperature(17)
                .precipitation(80)
                .status("Sunny");

        hourlyWeatherList.add(hourlyWeather1);
        hourlyWeatherList.add(hourlyWeather2);

        Location updatedLocation = locationRepository.save(location);
        assertThat(updatedLocation.getHourlyWeatherList()).isNotEmpty();

    }
}
