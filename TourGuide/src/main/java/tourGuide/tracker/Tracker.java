package tourGuide.tracker;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.helper.DateTimeHelper;
import tourGuide.service.TourGuideService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tracker {
    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final TourGuideService tourGuideService;
    private boolean stop = false;
    private UserApi userApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    public Tracker(TourGuideService tourGuideService, UserApi userApi) {
        this.tourGuideService = tourGuideService;
        this.userApi = userApi;
        executorService.submit(this::runTracking);
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
        //executorService.shutdown();
    }

    public void startTracking() {
        this.executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::runTracking);
        stop = false;
        logger.debug("restart tracker");
    }

    //@Override
    public void runTracking() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }


            List<User> users = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
            logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();
            //users.forEach(u -> tourGuideService.trackUserLocation(u));
            //tourGuideService.trackAllUserLocation(users);
            CompletableFuture completableFuture = tourGuideService.trackAllUserLocation(users);
            while (true) {
                if (completableFuture.isDone()) {
                    logger.debug("Tracking is done.");
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
