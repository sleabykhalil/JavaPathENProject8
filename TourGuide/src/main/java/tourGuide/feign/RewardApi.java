package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "reward-api", url = "http://localhost:8082")
@FeignClient(name = "reward-api", url = "http://reward.localhost:81")
public interface RewardApi {

    @GetMapping("/reward/getRewardPoints/{timeStamp}")
    int getRewardPoints(@PathVariable String timeStamp, @RequestParam String userId, @RequestParam String attractionId);
}
