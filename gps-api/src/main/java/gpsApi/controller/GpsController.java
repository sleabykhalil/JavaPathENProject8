package gpsApi.controller;

import gpsApi.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GpsController {
    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    @Autowired
    GpsService gpsService;

    @GetMapping("gps/attractions/{timeStamp}")
    public List<Attraction> getAllAttraction(@PathVariable String timeStamp) {
        logger.info("gps/attractions/timeStamp ={}", timeStamp);
        return gpsService.getAttractions();
    }

    @GetMapping("gps/attraction/{userId}/{timeStamp}")
    public VisitedLocation getUserAttraction(@PathVariable String userId, @PathVariable String timeStamp) {
        logger.info("gps/attractions/userId{}/timeStamp ={}", userId, timeStamp);
        return gpsService.getUserAttraction(userId);
    }
}
