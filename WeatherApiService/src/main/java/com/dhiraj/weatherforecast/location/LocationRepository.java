package com.dhiraj.weatherforecast.location;

import com.dhiraj.weatherapicommon.Location;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    List<Location> findUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.code = ?1 AND l.trashed = false")
    Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location SET trashed = TRUE WHERE code = ?1")
    void trashByCode(String code);
}
