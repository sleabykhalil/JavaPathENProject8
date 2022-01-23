package rewardApi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardApi.service.RewardService;

@RestController
public class RewardController {
    private Logger logger = LoggerFactory.getLogger(RewardController.class);

    @Autowired
    RewardService rewardService;

/*    @PostMapping("/reward/calculateRewards")
    public void calculateRewards(@RequestBody User user) {
        rewardService.calculateRewards(user);
    }

    @GetMapping("/reward/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(@RequestBody List<Attraction> attractions){
        return  rewardService.isWithinAttractionProximity(attractions);
    }*/

    @GetMapping("/reward/getRewardPoints/{timeStamp}")
    public int getRewardPoints(@PathVariable String timeStamp, @RequestParam String userId, @RequestParam String attractionId){
        logger.info("/reward/getRewardPoints/timeStamp={}",timeStamp);
        return rewardService.getRewardPoints(userId,attractionId);
    };
}

