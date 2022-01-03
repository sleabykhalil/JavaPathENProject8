package rewordApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewordApi.service.RewordService;

@RestController
public class RewordController {
    @Autowired
    RewordService rewordService;

/*    @PostMapping("/reword/calculateRewards")
    public void calculateRewards(@RequestBody User user) {
        rewordService.calculateRewards(user);
    }

    @GetMapping("/reword/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(@RequestBody List<Attraction> attractions){
        return  rewordService.isWithinAttractionProximity(attractions);
    }*/

    @GetMapping("/reword/getRewordPoints")
    public int getRewardPoints(@RequestParam String userId, @RequestParam String attractionId){
        return rewordService.getRewordPoints(userId,attractionId);
    };
}

