package com.dhiraj.weatherforecast;

import com.dhiraj.weatherapicommon.DailyWeather;
import com.dhiraj.weatherapicommon.HourlyWeather;
import com.dhiraj.weatherforecast.daily.DailyWeatherDTO;
import com.dhiraj.weatherforecast.hourly.HourlyWeatherDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = {"com.dhiraj.weatherapiservice", "com.dhiraj.weatherapicommon"})
public class WeatherApiServiceApplication {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        var typeMap1 = mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
        typeMap1.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        var typeMap2 = mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
        typeMap2.addMapping(HourlyWeatherDTO::getHourOfDay,
                (dest, value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));

        var typeMap3 = mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class);
        typeMap3.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
        typeMap3.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

        var typeMap4 = mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class);
        typeMap4.addMapping(DailyWeatherDTO::getDayOfMonth,
                (dest, value) -> dest.getId().setDayOfMonth(value != null ? (int) value : 0));
        typeMap4.addMapping(DailyWeatherDTO::getMonth,
                (dest, value) -> dest.getId().setMonth(value != null ? (int) value : 0));

        return mapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
