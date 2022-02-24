package rewardApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RewardService {
    private final RewardCentral rewardsCentral;

    /**
     * getReward point for user visiting attraction
     * @param userId user id
     * @param attractionId attraction id
     * @return reward point a integer
     */
    public int getRewardPoints(String userId, String attractionId) {
        return rewardsCentral.getAttractionRewardPoints(UUID.fromString(userId),UUID.fromString(attractionId));
    }
}
