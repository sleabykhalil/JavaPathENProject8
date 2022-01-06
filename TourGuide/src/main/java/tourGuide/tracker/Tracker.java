package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.service.TourGuideService;

public class Tracker extends Thread {
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final TourGuideService tourGuideService;
    private boolean stop = false;
    private UserApi userApi;

    public Tracker(TourGuideService tourGuideService, UserApi userApi) {
        this.tourGuideService = tourGuideService;
        this.userApi = userApi;
        executorService.submit(this);
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        //executorService.shutdownNow();
        executorService.shutdown();
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }

            List<User> users = userApi.getAllUsers();
            logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();
            //users.forEach(u -> tourGuideService.trackUserLocation(u));
            //tourGuideService.trackAllUserLocation(users);
            CompletableFuture completableFuture = tourGuideService.trackAllUserLocation(users);
            while (true) {
                if (completableFuture.isDone()) {
                    stopWatch.stop();
                    break;
                }
            }
            //stopWatch.stop();
            logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
            stopWatch.reset();
            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
