package com.dhiraj.weatherapicommon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @JsonIgnore
    public DailyWeather getShallowCopy() {
        DailyWeather copy = new DailyWeather();
        copy.setId(this.getId());
        return copy;
    }

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

    @Override
    public String toString() {
        return "DailyWeather [id=" + id + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp
                + ", precipitation=" + precipitation + ", status=" + status + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeather that = (DailyWeather) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
