package tourGuide.testPerformance;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTrackUsersPerformance {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    UserApi userApi;
    @Autowired
    RewardApi rewardApi;

    DateTimeHelper dateTimeHelper = new DateTimeHelper();
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
    public void highVolumeTrackLocation() throws InterruptedException {
        //given
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        int numberOfUsers = 100;
        if (System.getProperty("numberOfUsers")!= null && !System.getProperty("numberOfUsers").isEmpty()) {
            try {
                System.out.println("Users number pass from command line =" + System.getProperty("numberOfUsers"));
                numberOfUsers = Integer.parseInt(System.getProperty("numberOfUsers"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        InternalTestHelper.setInternalUserNumber(numberOfUsers);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        List<User> allUsers;
        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

        //when
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture completableFuture = new CompletableFuture();
        completableFuture = tourGuideService.trackAllUserLocation(allUsers);
        int counter = 0;
        while (true) {
            if (completableFuture.isDone() || counter >= allUsers.size()) {
                System.out.println("Number of tracked users = " + allUsers.size());
                stopWatch.stop();
                tourGuideService.tracker.stopTracking();
                break;
            } else {
                Thread.sleep(Math.max(Math.min(allUsers.size(),10000),500));
                if (counter != tourGuideService.getTrackUserMap().size()) {
                    counter = tourGuideService.getTrackUserMap().size();
                    System.out.println("Number of tracked users = " + counter);
                }
            }
        }
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        //then
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
