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

    public Location get(String code) {
        return locationRepository.findByCode(code);
    }

    public Location update(Location locationRequest) throws LocationNotFoundException {
        String code = locationRequest.getCode();
        Location locationInDB = locationRepository.findByCode(code);
        if (locationInDB == null) {
            throw new LocationNotFoundException("No location found with the code: " + code);
        }

        locationInDB.setCityName(locationRequest.getCityName());
        locationInDB.setRegionName(locationRequest.getRegionName());
        locationInDB.setCountryName(locationRequest.getCountryName());
        locationInDB.setCountryCode(locationRequest.getCountryCode());
        locationInDB.setEnabled(locationRequest.isEnabled());

        return locationRepository.save(locationInDB);

    }

    @Transactional
    public void delete(String code) throws LocationNotFoundException {
        Location locationCode = locationRepository.findByCode(code);

        if (locationCode == null) {
            throw new LocationNotFoundException("No location found with the code: " + code);
        }
        locationRepository.trashByCode(code);
    }
}
