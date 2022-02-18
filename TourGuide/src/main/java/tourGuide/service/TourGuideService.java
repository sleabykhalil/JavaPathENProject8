package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tourGuide.feign.GpsApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
@Lazy
public class TourGuideService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final RewardsService rewardsService;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;
    boolean testMode = true;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();
    GpsApi gpsApi;
    UserApi userApi;
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    ExecutorService getRewardExecutorService = Executors.newFixedThreadPool(600);

    private Map<String, Boolean> trackUserMap = new ConcurrentHashMap<>();

    public Map<String, Boolean> getTrackUserMap() {
        return trackUserMap;
    }

    public Map<String, Boolean> getCalculatedRewardForUserMap() {
        return rewardsService.getCalculatedRewardForUserMap();
    }

    @Autowired
    public TourGuideService(RewardsService rewardsService, GpsApi gpsApi, UserApi userApi) {

        this.gpsApi = gpsApi;
        this.userApi = userApi;
        this.rewardsService = rewardsService;

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userApi.initUser(dateTimeHelper.getTimeStamp(), InternalTestHelper.getInternalUserNumber());
            //initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this, userApi);
        addShutDownHook();
    }


    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = null;
        visitedLocation = (user.getVisitedLocations().size() > 0) ?
                getLastVisitedLocation(user.getVisitedLocations()) :
                trackUserLocation(user);
        return visitedLocation;
    }

    private VisitedLocation getLastVisitedLocation(List<VisitedLocation> visitedLocations) {
        return visitedLocations.get(visitedLocations.size() - 1);
    }


    public List<Provider> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public VisitedLocation trackUserLocation(User user) {

        VisitedLocation visitedLocation = gpsApi.getUserAttraction(user.getUserId().toString(), dateTimeHelper.getTimeStamp());
        userApi.addToVisitedLocations(dateTimeHelper.getTimeStamp(), user.getUserName(), visitedLocation.getTimeVisited().toString(), visitedLocation);
        getRewardExecutorService.submit(() -> {
            rewardsService.calculateRewards(user);
        });
        trackUserMap.putIfAbsent(user.getUserName(), true);
        return visitedLocation;
    }

    public CompletableFuture trackAllUserLocation(List<User> userList) {

        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (User user : userList) {
            completableFuture = completableFuture.thenCombine(CompletableFuture.supplyAsync(
                    () -> trackUserLocation(user), executorService), (x, y) -> null);
        }

        return completableFuture;
    }

    public void calculateRewardForPerfTest(List<User> userList) {

        for (User user : userList) {
            getRewardExecutorService.submit(() -> {
                rewardsService.calculateRewards(user);
            });
        }

    }

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();
        for (Attraction attraction : gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp())) {
            if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
                nearbyAttractions.add(attraction);
            }
        }

        return nearbyAttractions;
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();

    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}
