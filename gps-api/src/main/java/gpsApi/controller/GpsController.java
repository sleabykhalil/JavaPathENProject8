package gpsApi.controller;

import gpsApi.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GpsController {
    @Autowired
    GpsService gpsService;

    @GetMapping("gps/attractions")
    public List<Attraction> getAllAttraction() {
        return gpsService.getAttractions();
    }

    @GetMapping("gps/attraction/{userId}")
    public VisitedLocation getUserAttraction(@PathVariable String userId) {
        return gpsService.getUserAttraction(userId);
    }
}
