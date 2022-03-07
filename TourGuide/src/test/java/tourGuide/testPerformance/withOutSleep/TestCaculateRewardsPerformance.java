package tourGuide.testPerformance.withOutSleep;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestCaculateRewardsPerformance {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    UserApi userApi;
    @Autowired
    RewardApi rewardApi;

    DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @BeforeAll
    static void beforeAll() {
        TourGuideService.testMode = true;
    }

    @AfterAll
    static void afterAll() {
        TourGuideService.testMode = false;
    }
    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void highVolumeGetRewards() throws InterruptedException {
        //given
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        int numberOfUsers = 100;
        if (System.getProperty("numberOfUsers") != null && !System.getProperty("numberOfUsers").isEmpty()) {
            try {
                System.out.println("Users number pass from command line =" + System.getProperty("numberOfUsers"));
                numberOfUsers = Integer.parseInt(System.getProperty("numberOfUsers"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        InternalTestHelper.setInternalUserNumber(numberOfUsers);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        System.out.println("Start adding attraction for test");
        Attraction attraction = gpsApi.getAllAttractions(dateTimeHelper.getTimeStamp()).get(0);

        System.out.println("Add attraction is done");
        List<User> allUsers;
        allUsers = userApi.addFirstAttractionForAllUser(dateTimeHelper.getTimeStamp(), attraction);


        //when
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        tourGuideService.calculateRewardForPerfTest(allUsers);

        while (true) {
            if (tourGuideService.getCalculatedRewardForUserMap().size() >= allUsers.size()) {
                System.out.println("Number of calculated users = " + tourGuideService.getCalculatedRewardForUserMap().size());
                break;
            }
        }

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        //then
        int usersGerRewardsCount = (int) userApi.getAllUsers(dateTimeHelper.getTimeStamp()).stream()
                .filter(user -> user.getUserRewards().size() > 0).count();
        assertEquals(usersGerRewardsCount, InternalTestHelper.getInternalUserNumber());
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}
