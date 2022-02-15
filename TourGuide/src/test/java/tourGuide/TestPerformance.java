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
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void highVolumeTrackLocation() throws InterruptedException {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1000);
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
                System.out.println("Number of tracked users = " + allUsers.size());
                break;
            } else {
                Thread.sleep(Math.max( allUsers.size(),1000));
                System.out.println("Number of tracked users = " + userApi.getAllUsers(dateTimeHelper.getTimeStamp()).stream()
                        .filter(user -> user.getVisitedLocations().size() > 3).collect(Collectors.toList()).size());

            }
        }
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewards() throws ExecutionException, InterruptedException {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);

        System.out.println("Start adding attraction for test");
        userApi.initUserByAddVisitedLocation(dateTimeHelper.getTimeStamp(), attraction);
        System.out.println("Add attraction is done");

        List<User> allUsers;
        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
        rewardsService.calculateRewardsForListOfUser(allUsers);

        boolean firstTry = true;
        List<UUID> allUsersHasRewords;

        while ((allUsers.size() != 0) &&
                (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) <= TimeUnit.MINUTES.toSeconds(15))) {

            if (allUsers.size() > 0) {
                Thread.sleep(Math.max( allUsers.size(),10000));

                allUsersHasRewords = userApi.getAllUsers(dateTimeHelper.getTimeStamp())
                        .stream().filter(user -> user.getUserRewards().size() > 0).map(User::getUserId).collect(Collectors.toList());
                List<UUID> finalAllUsersHasRewords = allUsersHasRewords;

                allUsers.removeIf(user -> finalAllUsersHasRewords.contains(user.getUserId()));
                System.out.println("Number of calculated users = " +  allUsersHasRewords.size());
            }
            if ((allUsers.size() != 0) && firstTry &&
                    (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) > TimeUnit.MINUTES.toSeconds(10))) {
                rewardsService.calculateRewardsForListOfUser(allUsers);
                firstTry = false;
            }
        }

        assertEquals((int) userApi.getAllUsers(dateTimeHelper.getTimeStamp()).stream()
                .filter(user -> user.getUserRewards().size() > 0).count(), InternalTestHelper.getInternalUserNumber());

//        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
//        for (User user : allUsers) {
//            assertTrue(user.getUserRewards().size() > 0);
//        }
        System.out.println("Number of calculated users = " + (InternalTestHelper.getInternalUserNumber() - allUsers.size()));

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    private boolean userHasReward(User user) {
        try {
            return userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size() > 0;
        } catch (RetryableException ex) {
            //Boolean secondTry = userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size() > 0;
            ex.printStackTrace();
            //return secondTry;
        }
        return false;
    }

    @Test
    public void testRestartExecutorService() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final int[] i = {0, 0};
        executorService.submit(() -> {
            i[0]++;
        });
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            i[1]++;
        });
        assertEquals(1, i[0]);
        assertEquals(1, i[1]);
    }
}
