package com.dhiraj.weatherforecast.realtime;

import com.dhiraj.weatherapicommon.Location;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeolocationService.class);

    private final IP2Location ipLocator = new IP2Location();

    public GeolocationService(
            @Value("${ip2location.db-path:ip2locdb/IP2LOCATION-LITE-DB3.BIN}") String dbPath) {
        try {
            ipLocator.Open(resolveDbPath(dbPath));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * The path is relative to the working directory, so also try the module folder
     * to support starting the app from the repository root.
     */
    private static String resolveDbPath(String dbPath) {
        if (Files.exists(Path.of(dbPath))) {
            return dbPath;
        }
        String fromRoot = "WeatherApiService/" + dbPath;
        return Files.exists(Path.of(fromRoot)) ? fromRoot : dbPath;
    }

    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = ipLocator.IPQuery(ipAddress);

            if (!result.getStatus().equals("OK")) {
                throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
            }

            logger.info(result.toString());

            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(), result.getCountryShort());

        } catch (IOException e) {
            throw new GeolocationException("Error Querying IP database", e);
        }
    }

}
