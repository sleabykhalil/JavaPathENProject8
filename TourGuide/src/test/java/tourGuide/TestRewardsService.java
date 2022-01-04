package tourGuide;

import gpsUtil.GpsUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.UserApi;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.UserReward;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRewardsService {
    GpsApi gpsApi;
    UserApi userApi;
    RewordApi rewordApi;

    @Test
    public void userGetRewards() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi);

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsApi.getAllAttraction().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertEquals(1, userRewards.size());
    }

    @Test
    public void isWithinAttractionProximity() {
//        GpsUtil gpsUtil = new GpsUtil();
//        RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//        Attraction attraction = gpsApi.getAllAttraction().get(0);
//        assertTrue(rewordApi.isWithinAttractionProximity(attraction, attraction));
    }

    @Ignore // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        rewardsService.calculateRewards(userApi.getAllUsers().get(0));
        List<UserReward> userRewards = userApi.getUserRewords(userApi.getAllUsers().get(0));
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
    }

}
