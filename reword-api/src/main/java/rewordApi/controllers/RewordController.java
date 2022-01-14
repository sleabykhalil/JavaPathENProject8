package rewordApi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewordApi.service.RewordService;

@RestController
public class RewordController {
    private Logger logger = LoggerFactory.getLogger(RewordController.class);

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

    @GetMapping("/reword/getRewordPoints/{timeStamp}")
    public int getRewardPoints(@PathVariable String timeStamp, @RequestParam String userId, @RequestParam String attractionId){
        logger.info("/reword/getRewordPoints/timeStamp={}",timeStamp);
        return rewordService.getRewordPoints(userId,attractionId);
    };
}

