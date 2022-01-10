package tourGuide.service;

import gpsUtil.GpsUtil;
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
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
@Lazy
public class TourGuideService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;
    boolean testMode = true;
    GpsApi gpsApi;
    UserApi userApi;
    ExecutorService executor = Executors.newFixedThreadPool(1500);
    final Map<String, Integer> testTracingTimes = new ConcurrentHashMap<>();

    @Autowired
    public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, GpsApi gpsApi, UserApi userApi) {

        this.gpsApi = gpsApi;
        this.userApi = userApi;

        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userApi.initUser(InternalTestHelper.getInternalUserNumber());
            //initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this, userApi);
        addShutDownHook();
    }


    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
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
        addToTestMap(user);
        VisitedLocation visitedLocation = gpsApi.getUserAttraction(user.getUserId().toString());
        //user.addToVisitedLocations(visitedLocation);
        userApi.addToVisitedLocations(user.getUserName(),visitedLocation);
        rewardsService.calculateRewards(user);
/*        try {
            rewardsService.calculateRewards(user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        return visitedLocation;
    }

    private void addToTestMap(User user) {
        if (testTracingTimes.containsKey(user.getUserName()))
            testTracingTimes.put(user.getUserName(), testTracingTimes.get(user.getUserName()) + 1);
        else
            testTracingTimes.put(user.getUserName(), 1);
    }

    public Map<String, Integer> getTestTracingTimes() {
        return testTracingTimes;
    }

    public CompletableFuture trackAllUserLocation(List<User> userList) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (User user : userList) {
            completableFuture = completableFuture.thenCombine(CompletableFuture.supplyAsync(
                    () -> trackUserLocation(user), executor), (x, y) -> null);
        }
/*        while (true) {
            if (completableFuture.isDone()) {
                logger.info("tracking all user done");
                System.out.println("I am in service but fin");
                break;
            }
        }*/
        return completableFuture;
    }

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();
        for (Attraction attraction : gpsApi.getAllAttraction()) {
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
