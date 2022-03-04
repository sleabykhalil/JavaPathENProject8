package gpsApi.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GpsService {
    private final GpsUtil gpsUtil;

    /**
     * Get all attractions
     * @return list of attraction
     */
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

    /**
     * Get user location
     * @param userId user id for user to get his location
     * @return user location
     */
    public VisitedLocation getUserLocation(String userId) {
        UUID userIdAsUUID=UUID.fromString(userId);
        return gpsUtil.getUserLocation(userIdAsUUID);
    }
}
