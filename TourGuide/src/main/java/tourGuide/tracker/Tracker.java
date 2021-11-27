package tourGuide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker extends Thread {
    public static final int NUMBER_OF_USER_BY_THREAD = 100;

    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final TourGuideService tourGuideService;

    /* with newCachedThreadPool thread will stop after 60s automatically **60L, TimeUnit.SECONDS,**
    private boolean stop = false;
     */
    public Tracker(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
        TrackUsers();
    }

    public void TrackUsers() {

        List<List<User>> usersList = new ArrayList<>();

        int start = 0;
        int end = 0;
        int i = 0;

        do {
            start = end;
            end = start + NUMBER_OF_USER_BY_THREAD;

            usersList.add(tourGuideService.getAllUsers().
                    subList(start, Math.min(end, tourGuideService.getAllUsers().size())));
            int finalI = i;
            executorService.submit(() -> {
                StopWatch stopWatch = new StopWatch();

                logger.debug("Begin Tracker. Tracking " + usersList.get(finalI).size() + " users.");
                stopWatch.start();
                usersList.get(finalI).forEach(u -> tourGuideService.trackUserLocation(u));
                stopWatch.stop();
                logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
                stopWatch.reset();

            });

            i++;
        } while (end < tourGuideService.getAllUsers().size());

    }

    /**
     * Assures to shut down the Tracker thread
     * No need after using with newCachedThreadPool , thread will automatically stop
     */
    public void stopTracking() {
        logger.debug(" shutdown of stop tracking");
    }
}
