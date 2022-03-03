package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reward-api", url =  "${feign.reward.url}")
public interface RewardApi {

    @GetMapping("/reward/getRewardPoints/{timeStamp}")
    int getRewardPoints(@PathVariable String timeStamp, @RequestParam String userId, @RequestParam String attractionId);
}
