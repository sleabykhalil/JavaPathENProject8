package rewordApi.controllers;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewordApi.model.User;
import rewordApi.service.RewordService;

@RestController
public class RewordController {
    @Autowired
    RewordService rewordService;

    @PostMapping("/reword/calculateRewards")
    public void calculateRewards(@RequestParam User user) {
        rewordService.calculateRewards(user);
    }

    @GetMapping("/reword/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(@RequestParam Attraction attraction,@RequestParam Location location){
        return  rewordService.isWithinAttractionProximity(attraction,location);
    }

}

