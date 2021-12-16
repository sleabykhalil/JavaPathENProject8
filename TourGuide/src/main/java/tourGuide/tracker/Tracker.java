package tourGuide.tracker;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker extends Thread {
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final TourGuideService tourGuideService;
    private boolean stop = false;

    public Tracker(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
        int start = 0;
        int end = 0;
        do {
            end = start + 100;
            executorService.submit(Objects.requireNonNull(trackUsers(tourGuideService.getAllUsers().subList(start, Math.min(end, tourGuideService.getAllUsers().size())))));
            start = end;
        } while (end < tourGuideService.getAllUsers().size());


    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
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
        userList.parallelStream().forEach(u -> tourGuideService.trackUserLocation(u));
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
