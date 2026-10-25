package com.dhiraj.weatherapicommon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Column(length = 12, nullable = false, unique = true)
    @Id
    private String code;

    @Column(length = 128, nullable = false)
    private String cityName;

    @Column(length = 128)
    private String regionName;

    @Column(length = 64, nullable = false)
    private String countryName;

    @Column(length = 2, nullable = false)
    private String countryCode;

    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HourlyWeather> hourlyWeatherList = new ArrayList<>();

    public Location(String cityName, String regionName, String countryName, String countryCode) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return cityName + ", " + (regionName != null ? regionName + ", " : "") + countryName;
    }

    public Location code(String code) {
        setCode(code);
        return this;
    }

}
