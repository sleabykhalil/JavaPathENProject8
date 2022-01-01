package tourGuide.feign;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tourGuide.user.User;

@FeignClient(value = "reword-Api", url = "localhost:8082")
public interface RewordApi {

    @PostMapping("/reword/calculateRewards")
    void calculateRewards(User user);

    @GetMapping("/reword/isWithinAttractionProximity")
    boolean isWithinAttractionProximity(Attraction attraction, Location location);

}
