package com.dhiraj.weatherapicommon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DailyWeatherId implements Serializable {

    private int dayOfMonth;
    private int month;

    @ManyToOne
    @JoinColumn(name = "location_code")
    @JsonIgnore
    private Location location;

    @Override
    public String toString() {
        return "DailyWeatherId [dayOfMonth=" + dayOfMonth + ", month=" + month + ", location=" + location + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeatherId that = (DailyWeatherId) o;
        return dayOfMonth == that.dayOfMonth && month == that.month && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfMonth, month, location);
    }
}
