package com.dhiraj.weatherforecast.realtime;

import com.dhiraj.weatherapicommon.Location;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeolocationService.class);

    private final IP2Location ipLocator = new IP2Location();

    public GeolocationService() {
        try {
            String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";
            ipLocator.Open(DBPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = ipLocator.IPQuery(ipAddress);

            if (!result.getStatus().equals("OK")) {
                throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
            }

            logger.info(result.toString());

            return new Location(result.getCity(), result.getRegion(), result.getCountryShort(), result.getCountryShort());

        } catch (IOException e) {
            throw new GeolocationException("Error Querying IP database", e);
        }
    }


}