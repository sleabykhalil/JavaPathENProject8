package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reword-api", url = "http://localhost:8082")
public interface RewordApi {

/*    @PostMapping("/reword/calculateRewards")
    void calculateRewards(@RequestBody User user);

    @GetMapping("/reword/isWithinAttractionProximity")
    boolean isWithinAttractionProximity(@RequestBody List<Attraction> attractions);*/

    @GetMapping("/reword/getRewordPoints")
    int getRewardPoints(@RequestParam String userId, @RequestParam String attractionId);
}
