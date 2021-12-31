package gpsApi.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsService {
    private final GpsUtil gpsUtil = new GpsUtil();

    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

    public VisitedLocation getUserAttraction(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }
}
