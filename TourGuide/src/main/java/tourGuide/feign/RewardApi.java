package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reward-api", url = "http://localhost:8082")
public interface RewardApi {

/*    @PostMapping("/reward/calculateRewards")
    void calculateRewards(@RequestBody User user);

    @GetMapping("/reward/isWithinAttractionProximity")
    boolean isWithinAttractionProximity(@RequestBody List<Attraction> attractions);*/

    @GetMapping("/reward/getRewardPoints/{timeStamp}")
    int getRewardPoints(@PathVariable String timeStamp, @RequestParam String userId, @RequestParam String attractionId);
}
