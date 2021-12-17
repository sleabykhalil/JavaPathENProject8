package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class GpsUnitService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private GpsUtil gpsUtil = new GpsUtil();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

    public VisitedLocation getUserLocation(UUID userId) {
        Future<VisitedLocation> visitedLocationFuture = executorService.submit(() -> {
                    logger.info("get user location user: {}", userId.toString());

                    return gpsUtil.getUserLocation(userId);
                }
        );
        try {
            return visitedLocationFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        logger.error("VisitedLocation cannot be return from Gps ");
        return null;
    }

}
