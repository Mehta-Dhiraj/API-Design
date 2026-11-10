package com.dhiraj.weatherapicommon;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "weather_daily")
public class DailyWeather {

    @EmbeddedId
    private DailyWeatherId id = new DailyWeatherId();

    private int minTemp;
    private int maxTemp;
    private int precipitation;

    @Column(length = 50)
    private String status;


    public DailyWeather precipitation(int pPrecipitation) {
        setPrecipitation(pPrecipitation);
        return this;
    }

    public DailyWeather status(String pStatus) {
        setStatus(pStatus);
        return this;
    }

    public DailyWeather location(Location location) {
        this.id.setLocation(location);
        return this;
    }

    public DailyWeather minTemp(int pMinTemp) {
        setMinTemp(pMinTemp);
        return this;
    }

    public DailyWeather maxTemp(int pMaxTemp) {
        setMaxTemp(pMaxTemp);
        return this;
    }

    public DailyWeather dayOfMonth(int pDayOfMonth) {
        this.id.setDayOfMonth(pDayOfMonth);
        return this;
    }

    public DailyWeather month(int pMonth) {
        this.id.setMonth(pMonth);
        return this;
    }
}
