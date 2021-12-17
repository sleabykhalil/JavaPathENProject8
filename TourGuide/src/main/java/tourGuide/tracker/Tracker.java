package tourGuide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.service.UserServices;
import tourGuide.user.User;

public class Tracker extends Thread {
    private static final int NUMBER_OF_USER_BY_THREAD = 100;
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final TourGuideService tourGuideService;
    private final UserServices userServices;
    private boolean stop = false;

    public Tracker(TourGuideService tourGuideService, UserServices userServices) {
        this.userServices = userServices;
        this.tourGuideService = tourGuideService;
        List<List<User>> usersList = new ArrayList<List<User>>();

        int start = 0;
        int end = 0;
        int i = 0;

        do {
            start = end;
            end = start + NUMBER_OF_USER_BY_THREAD;

            usersList.add(userServices.getAllUsers().
                    subList(start, Math.min(end, userServices.getAllUsers().size())));
            int finalI = i;
            executorService.submit(() -> {
                StopWatch stopWatch = new StopWatch();
//                        while (true) {
//                            if (Thread.currentThread().isInterrupted() || stop) {
//                                logger.debug("Tracker stopping");
//                                logger.debug(" shutdown of stop flag");
//
//                                break;
//                            }
                logger.debug("Begin Tracker. Tracking " + usersList.get(finalI).size() + " users.");
                stopWatch.start();
                //todo the same function that we has to test !!!!
                usersList.get(finalI).forEach(u -> userServices.trackUserLocation(u));
                stopWatch.stop();
                logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
                stopWatch.reset();
                try {
                    logger.debug("Tracker sleeping");
                    TimeUnit.SECONDS.sleep(trackingPollingInterval);
                    logger.debug("Tracker finished");
                } catch (InterruptedException e) {
                    logger.error("InterruptedException threw ::: " + e);
                    // break;
                }
            });

            i++;
        } while (end < userServices.getAllUsers().size());

    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        // executorService.shutdownNow();
    }

    private Runnable trackUsers(List<User> userList) {
        StopWatch stopWatch = new StopWatch();
/*        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }*/

        //List<User> users = tourGuideService.getAllUsers();

        logger.debug("Begin Tracker. Tracking " + userList.size() + " users.");
        stopWatch.start();
        userList.parallelStream().forEach(u -> userServices.trackUserLocation(u));
        stopWatch.stop();
        logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        stopWatch.reset();
        try {
            logger.debug("Tracker sleeping");
            TimeUnit.SECONDS.sleep(trackingPollingInterval);
        } catch (InterruptedException e) {
            logger.debug("Tracker Exception");
            //break;
        }
        // }

        return null;
    }
}
