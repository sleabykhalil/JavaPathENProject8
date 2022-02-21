package tourGuide;

import gpsUtil.GpsUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestRewardsService {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    UserApi userApi;
    @Autowired
    RewardApi rewardApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Test
    public void userGetRewards() throws ExecutionException, InterruptedException {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userApi.addUser(dateTimeHelper.getTimeStamp(), user);

        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        userApi.addToVisitedLocations(dateTimeHelper.getTimeStamp(),
                user.getUserName(),
                dateTimeHelper.getTimeStamp(), new VisitedLocation(user.getUserId(), attraction, new Date()));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        tourGuideService.trackUserLocation(userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()));
        //rewardsService.calculateRewards(userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp())).get();
        rewardsService.calculateRewards(userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()));
        int userRewardsSize;
        do {
            userRewardsSize = userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size();
        } while ((userRewardsSize == 0) &&
                (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) < TimeUnit.SECONDS.toSeconds(1)));
        stopWatch.stop();

        tourGuideService.tracker.stopTracking();
        assertEquals(1, userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size());
    }

    @Test //need to verify
    public void isWithinAttractionProximity() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    //  @Ignore // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() throws ExecutionException, InterruptedException {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);
        User user = userApi.getAllUsers(dateTimeHelper.getTimeStamp()).get(0);
        //rewardsService.calculateRewards(user).get();
        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = userApi.getUserRewardsById(user.getUserName(), dateTimeHelper.getTimeStamp());
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).size(), userRewards.size());
    }

}
