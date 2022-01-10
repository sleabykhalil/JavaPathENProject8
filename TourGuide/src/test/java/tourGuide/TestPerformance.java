package tourGuide;

import gpsUtil.GpsUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    RewordApi rewordApi;
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
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        List<User> allUsers = new ArrayList<>();
        allUsers = userApi.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
/*        for (User user : allUsers) {
            tourGuideService.trackUserLocation(user);
        }*/
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


        assertEquals(tourGuideService.getTestTracingTimes().size(), allUsers.size());
//        assertTrue(tourGuideService.getTestTracingTimes().containsValue(2));
//        assertFalse(tourGuideService.getTestTracingTimes().containsValue(1));

        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    //@Ignore
    @Test
    public void highVolumeGetRewards() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(10000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        Attraction attraction = gpsApi.getAllAttraction().get(0);
        List<User> allUsers;
        allUsers = userApi.getAllUsers();
        allUsers.forEach(u -> {
            //u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
            userApi.addToVisitedLocations(u.getUserName(),new VisitedLocation(u.getUserId(), attraction, new Date()));
        });
        allUsers = userApi.getAllUsers();

        allUsers.forEach(u -> rewardsService.calculateRewards(u));

//        CompletableFuture calculateRewardsForListOfUser = rewardsService.calculateRewardsForListOfUser(allUsers);
//        while (true) {
//            if (calculateRewardsForListOfUser.isDone()) {
////                stopWatch.stop();
////                tourGuideService.tracker.stopTracking();
//                System.out.println("calculation done ");
//                break;
//            }
//        }

        for (User user : allUsers) {
            assertTrue(userApi.getUserByUserName(user.getUserName()).getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
