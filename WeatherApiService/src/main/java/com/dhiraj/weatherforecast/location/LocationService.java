package com.dhiraj.weatherforecast.location;

import com.dhiraj.weatherapicommon.Location;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> list() {
        return locationRepository.findUnTrashed();
    }

    public Location get(String code) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(code);
        if (location == null) {
            throw new LocationNotFoundException(code);
        }

        return location;
    }

    public Location update(Location locationRequest) throws LocationNotFoundException {
        String code = locationRequest.getCode();
        Location locationInDB = get(code);

        locationInDB.setCityName(locationRequest.getCityName());
        locationInDB.setRegionName(locationRequest.getRegionName());
        locationInDB.setCountryName(locationRequest.getCountryName());
        locationInDB.setCountryCode(locationRequest.getCountryCode());
        locationInDB.setEnabled(locationRequest.isEnabled());

        return locationRepository.save(locationInDB);

    }

    @Transactional
    public void delete(String code) throws LocationNotFoundException {
        get(code);
        locationRepository.trashByCode(code);
    }
}
