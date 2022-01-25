package tourGuide;

import feign.RetryableException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestPerformance {
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

    // @Ignore
    @Test
    public void highVolumeTrackLocation() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        List<User> allUsers;
        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture completableFuture = new CompletableFuture();
        completableFuture = tourGuideService.trackAllUserLocation(allUsers);
        while (true) {
            if (completableFuture.isDone()) {
                stopWatch.stop();
                tourGuideService.tracker.stopTracking();
                break;
            }
        }
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    //@Ignore
    @Test
    public void highVolumeGetRewards() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        List<User> allUsers;
        System.out.println("Start adding attraction for test");
        userApi.initUserByAddVisitedLocation(dateTimeHelper.getTimeStamp(), attraction);

        System.out.println("Add attraction is done");

        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
        rewardsService.calculateRewardsForListOfUser(allUsers);
        boolean firstTry = true;
        while ((allUsers.size() != 0) &&
                (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) <= TimeUnit.MINUTES.toSeconds(20))) {

            allUsers.removeIf(user -> userHasReward(user));
            System.out.println("##############" + allUsers.size() + "###############" + TimeUnit.MILLISECONDS.toMinutes(stopWatch.getTime()) + "#######");
            if ((allUsers.size() != 0) && firstTry &&
                    (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) > TimeUnit.MINUTES.toSeconds(15))) {
                rewardsService.calculateRewardsForListOfUser(allUsers);
                firstTry = false;
            }
        }

        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    private boolean userHasReward(User user) {
        try {
            return userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size() > 0;
        } catch (RetryableException ex) {
            Boolean secondTry = userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size() > 0;
            ex.printStackTrace();
            return secondTry;
        }
    }

    @Test
    public void testThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            int i = 0;
            while (i < 1000000) {
                System.out.print(i++);
            }
        });
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            int i = 0;
            System.out.print("###################################################################");

            while (i < 1000000) {
                System.out.print(i++);
            }
        });

    }
}
