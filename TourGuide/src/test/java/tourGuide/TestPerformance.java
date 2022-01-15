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
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
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
    RewordApi rewordApi;
    ExecutorService executorService = Executors.newCachedThreadPool();
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
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        List<User> allUsers = new ArrayList<>();
        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

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


//        assertEquals(tourGuideService.getTestTracingTimes().size(), allUsers.size());
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
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);
        // tourGuideService.tracker.stopTracking();
        Attraction attraction = gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()).get(0);
        List<User> allUsers;
        // allUsers = userApi.getAllUsers(new Date().toString());
        System.out.println("Start adding attraction for test");
        //VisitedLocation visitedLocation = new VisitedLocation(u.getUserId(), attraction, new Date());
        userApi.initUserByAddVisitedLocation(dateTimeHelper.getTimeStamp(), attraction);
//        allUsers.forEach(u ->
//                executorService.submit(() -> {
//                    VisitedLocation visitedLocation = new VisitedLocation(u.getUserId(), attraction, new Date());
//                    userApi.addToVisitedLocations(dateTimeHelper.getTimeStamp(), u.getUserName(),
//                            visitedLocation.getTimeVisited().toString(), visitedLocation);
//
//                }));
        System.out.println("Add attraction is done");
        //       stopWatch.start();

        //  tourGuideService.tracker.startTracking();


        // allUsers.forEach(u -> rewardsService.calculateRewards(u));
//
////        CompletableFuture calculateRewardsForListOfUser = rewardsService.calculateRewardsForListOfUser(allUsers);
////        while (true) {
////            if (calculateRewardsForListOfUser.isDone()) {
////                System.out.println("calculation done ");
////                break;
////            }
////        }
//        allUsers = userApi.getAllUsers(new Date().toString());
//
//        CompletableFuture completableFuture = new CompletableFuture();
//        completableFuture = rewardsService.calculateRewardsForListOfUser(allUsers);
//
//        while (true) {
//            if (completableFuture.isDone()) {
//                //stopWatch.stop();
//                //tourGuideService.tracker.stopTracking();
//                break;
//            }
//        }
//        try {
//            completableFuture.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//        allUsers.parallelStream().forEach(u -> rewardsService.calculateRewards(u));
        allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
        rewardsService.calculateRewardsForListOfUser(allUsers);
        boolean firstTry = true;
        while ((allUsers.size() != 0) &&
                (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) <= TimeUnit.MINUTES.toSeconds(20))) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(allUsers.size());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            allUsers.removeIf(user -> userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserRewards().size() > 0);
            System.out.println("##############" + allUsers.size() + "###############" + TimeUnit.MILLISECONDS.toMinutes(stopWatch.getTime()) + "#######");
            if ((allUsers.size() != 0) && firstTry &&
                    (TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) > TimeUnit.MINUTES.toSeconds(15))){
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
