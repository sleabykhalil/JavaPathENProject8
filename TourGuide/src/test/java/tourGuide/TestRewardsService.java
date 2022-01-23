package tourGuide;

import gpsUtil.GpsUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRewardsService {
    GpsApi gpsApi;
    UserApi userApi;
    RewardApi rewardApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Test
    public void userGetRewards() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertEquals(1, userRewards.size());
    }

    @Test //need to verify
    public void isWithinAttractionProximity() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    @Ignore // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        rewardsService.calculateRewards(userApi.getAllUsers(dateTimeHelper.getTimeStamp()).get(0));
        List<UserReward> userRewards = userApi.getUserReward(dateTimeHelper.getTimeStamp(), userApi.getAllUsers(new Date().toString()).get(0));
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
    }

}
