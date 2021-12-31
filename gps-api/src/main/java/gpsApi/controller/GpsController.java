package gpsApi.controller;

import gpsApi.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {
    @Autowired
    GpsService gpsService;

    @GetMapping("gps/attractions")
    public List<Attraction> getAllAttraction() {
        return gpsService.getAttractions();
    }

    @GetMapping("gps/attraction")
    public VisitedLocation getUserAttraction(UUID userId){
        return gpsService.getUserAttraction(userId);
    }
}
