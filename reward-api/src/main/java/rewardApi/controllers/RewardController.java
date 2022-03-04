package rewardApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardApi.service.RewardService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RewardController {
    private Logger logger = LoggerFactory.getLogger(RewardController.class);

    private final RewardService rewardService;

    @Operation(summary = "Get all attractions")
    @GetMapping("/reward/getRewardPoints/{timeStamp}")
    public int getRewardPoints(@Parameter(description = "TimeStamp", required = true)
                               @PathVariable String timeStamp,
                               @Parameter(description = "User UUID as string", required = true)
                               @RequestParam String userId,
                               @Parameter(description = "Attraction UUID as string", required = true)
                               @RequestParam String attractionId) {
        logger.info("/reward/getRewardPoints/timeStamp={}", timeStamp);
        return rewardService.getRewardPoints(userId, attractionId);
    }

}

