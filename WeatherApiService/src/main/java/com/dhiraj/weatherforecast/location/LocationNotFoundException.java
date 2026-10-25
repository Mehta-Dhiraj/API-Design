package com.dhiraj.weatherforecast.location;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String locationCode) {
        super("No location found with then given code: " + locationCode);
    }

    public LocationNotFoundException(String countryCode, String cityName) {
        super("No Location found with given country code: " + countryCode + " and city name: " + cityName);
    }
}
